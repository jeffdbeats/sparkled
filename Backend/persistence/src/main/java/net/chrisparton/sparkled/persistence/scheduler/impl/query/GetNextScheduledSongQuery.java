package net.chrisparton.sparkled.persistence.scheduler.impl.query;

import net.chrisparton.sparkled.entity.ScheduledSong;
import net.chrisparton.sparkled.entity.ScheduledSong_;
import net.chrisparton.sparkled.persistence.PersistenceQuery;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.Optional;

public class GetNextScheduledSongQuery implements PersistenceQuery<Optional<ScheduledSong>> {


    @Override
    public Optional<ScheduledSong> perform(EntityManager entityManager) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ScheduledSong> cq = cb.createQuery(ScheduledSong.class);
        Root<ScheduledSong> root = cq.from(ScheduledSong.class);

        cq.where(
                cb.greaterThan(root.get(ScheduledSong_.startTime), new Date())
        );

        cq.orderBy(
                cb.asc(root.get(ScheduledSong_.startTime))
        );

        TypedQuery<ScheduledSong> query = entityManager.createQuery(cq);
        query.setMaxResults(1);

        try {
            ScheduledSong scheduledSong = query.getSingleResult();
            return Optional.ofNullable(scheduledSong);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
