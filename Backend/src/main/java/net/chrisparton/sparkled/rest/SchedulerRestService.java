package net.chrisparton.sparkled.rest;

import net.chrisparton.sparkled.entity.ScheduledSong;
import net.chrisparton.sparkled.entity.Song;
import net.chrisparton.sparkled.persistence.scheduler.ScheduledSongPersistenceService;
import net.chrisparton.sparkled.viewmodel.ScheduledSongViewModel;
import net.chrisparton.sparkled.viewmodel.converter.ScheduledSongViewModelConverter;
import org.apache.commons.lang3.time.DateUtils;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Path("/scheduler")
public class SchedulerRestService extends RestService {

    private static final SimpleDateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static final int MIN_SECONDS_BETWEEN_SONGS = 5;
    private static final int MIN_SECONDS_IN_FUTURE = 30;

    private ScheduledSongPersistenceService persistenceService = new ScheduledSongPersistenceService();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getScheduledSongsForDay(@QueryParam("date") String date) {
        if (date == null) {
            String message = "Date must be provided.";
            return getJsonResponse(Response.Status.BAD_REQUEST, message);
        }

        Date parsedDay;
        try {
            parsedDay = dayFormat.parse(date);
        } catch (ParseException e) {
            String message = "Date format must be '" + dayFormat.toPattern() + "'.";
            return getJsonResponse(Response.Status.BAD_REQUEST, message);
        }

        Date startDay = DateUtils.truncate(parsedDay, Calendar.DATE);
        Date endDay = DateUtils.addDays(startDay, 1);
        endDay = DateUtils.addMilliseconds(endDay, -1);
        List<ScheduledSong> scheduledSongs = persistenceService.getScheduledSongs(startDay, endDay);

        ScheduledSongViewModelConverter converter = new ScheduledSongViewModelConverter();
        List<ScheduledSongViewModel> viewModels = scheduledSongs.stream()
                .map(converter::toViewModel)
                .collect(Collectors.toList());

        return getJsonResponse(viewModels);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response scheduleSong(ScheduledSongViewModel viewModel) {
        ScheduledSong scheduledSong = new ScheduledSongViewModelConverter().fromViewModel(viewModel);

        Date songStartTime = scheduledSong.getStartTime();
        Date earliestStartTime = DateUtils.addSeconds(new Date(), MIN_SECONDS_IN_FUTURE);
        if (songStartTime == null || songStartTime.before(earliestStartTime)) {
            String message = "Songs must be scheduled at least " + MIN_SECONDS_IN_FUTURE + " seconds in the future.";
            return getJsonResponse(Response.Status.BAD_REQUEST, message);
        }

        Song song = scheduledSong.getSong();
        if (song != null) {
            Date offsetStartTime = DateUtils.addSeconds(songStartTime, -MIN_SECONDS_BETWEEN_SONGS);
            int seconds = (int) Math.ceil(song.getDurationFrames() / song.getFramesPerSecond());
            seconds += MIN_SECONDS_BETWEEN_SONGS;
            Date endTime = DateUtils.addSeconds(offsetStartTime, seconds);
            List<ScheduledSong> overlappingSongs = persistenceService.getScheduledSongs(offsetStartTime, endTime);

            if (!overlappingSongs.isEmpty()) {
                String message = "Scheduled songs cannot overlap, and must have at least a " + MIN_SECONDS_BETWEEN_SONGS + " second gap between them.";
                return getJsonResponse(Response.Status.BAD_REQUEST, message);
            }
        }

        boolean success = persistenceService.saveScheduledSong(scheduledSong);
        return getResponse(success ? Response.Status.OK : Response.Status.BAD_REQUEST);
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response unscheduleSong(@PathParam("id") int id) {
        boolean success = persistenceService.removeScheduledSong(id);
        return getResponse(success ? Response.Status.OK : Response.Status.BAD_REQUEST);
    }
}