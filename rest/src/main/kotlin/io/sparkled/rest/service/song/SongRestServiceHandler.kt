package io.sparkled.rest.service.song

import com.google.gson.JsonSyntaxException
import io.sparkled.persistence.song.SongPersistenceService
import io.sparkled.persistence.transaction.Transaction
import io.sparkled.rest.response.IdResponse
import io.sparkled.rest.service.RestServiceHandler
import io.sparkled.viewmodel.exception.ViewModelConversionException
import io.sparkled.viewmodel.song.SongViewModel
import io.sparkled.viewmodel.song.SongViewModelConverter
import java.io.ByteArrayOutputStream
import java.io.InputStream
import javax.inject.Inject
import javax.ws.rs.core.Response

class SongRestServiceHandler
@Inject constructor(
    private val transaction: Transaction,
    private val songPersistenceService: SongPersistenceService,
    private val songViewModelConverter: SongViewModelConverter
) : RestServiceHandler() {

    fun createSong(songViewModelJson: String, inputStream: InputStream): Response {
        return transaction.of {
            val songViewModel = getSongViewModel(songViewModelJson)
            songViewModel.setId(null)

            var song = songViewModelConverter.toModel(songViewModel)
            val audioData = loadAudioData(inputStream)
            song = songPersistenceService.createSong(song, audioData)

            return@of respondOk(IdResponse(song.getId()!!))
        }
    }

    private fun getSongViewModel(songViewModelJson: String): SongViewModel {
        try {
            return gson.fromJson(songViewModelJson, SongViewModel::class.java)
        } catch (e: JsonSyntaxException) {
            throw ViewModelConversionException("Song data is invalid.", e)
        }
    }

    private fun loadAudioData(inputStream: InputStream): ByteArray {
        val outputStream = ByteArrayOutputStream()
        inputStream.copyTo(outputStream)
        return outputStream.toByteArray()
    }

    fun getAllSongs(): Response {
        val songs = songPersistenceService.getAllSongs()
        val results = songs.asSequence().map(songViewModelConverter::toViewModel).toList()

        return respondOk(results)
    }

    fun getSong(songId: Int): Response {
        val song = songPersistenceService.getSongById(songId)

        if (song != null) {
            val viewModel = songViewModelConverter.toViewModel(song)
            return respondOk(viewModel)
        }

        return respond(Response.Status.NOT_FOUND, "Song not found.")
    }

    fun deleteSong(id: Int): Response {
        return transaction.of {
            songPersistenceService.deleteSong(id)
            return@of respondOk()
        }
    }
}