package io.sparkled.persistence.playlist

import io.sparkled.model.entity.Playlist
import io.sparkled.model.entity.PlaylistSequence
import io.sparkled.model.entity.Sequence
import io.sparkled.model.playlist.PlaylistSummary
import java.util.Optional
import java.util.UUID

interface PlaylistPersistenceService {

    fun createPlaylist(playlist: Playlist): Playlist

    fun getAllPlaylists(): List<Playlist>

    fun getPlaylistSummaries(): Map<Int, PlaylistSummary>

    fun getPlaylistById(playlistId: Int): Optional<Playlist>

    fun getSequenceAtPlaylistIndex(playlistId: Int, index: Int): Optional<Sequence>

    fun getPlaylistSequencesByPlaylistId(playlistId: Int): List<PlaylistSequence>

    fun getPlaylistSequenceByUuid(sequenceId: Int, uuid: UUID): Optional<PlaylistSequence>

    fun savePlaylist(playlist: Playlist, playlistSequences: List<PlaylistSequence>)

    fun deletePlaylist(playlistId: Int)
}
