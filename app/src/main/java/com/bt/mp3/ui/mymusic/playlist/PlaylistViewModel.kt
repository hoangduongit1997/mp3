package com.bt.mp3.ui.mymusic.playlist

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import com.bt.base.ui.BaseViewModel
import com.bt.mp3.annotation.DefaultDispatcher
import com.bt.mp3.domain.usecase.GetPlaylistUseCase
import com.bt.mp3.model.PlaylistItem
import com.bt.mp3.model.PlaylistItemMapper
import com.bt.mp3.model.PlaylistTypeItem
import kotlinx.coroutines.CoroutineDispatcher

class PlaylistViewModel @ViewModelInject constructor(
    private val getPlaylistUseCase: GetPlaylistUseCase,
    private val playlistItemMapper: PlaylistItemMapper,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : BaseViewModel() {

    private val _playlistType = MutableLiveData<Int>()
    val playlistType: LiveData<Int>
        get() = _playlistType

    val playlists: LiveData<List<PlaylistItem>> = _playlistType.switchMap {
        liveData(defaultDispatcher) {
            val data = getPlaylistUseCase.execute(GetPlaylistUseCase.Param(type = it)).map {
                playlistItemMapper.mapToPresentation(it)
            }

            emit(data)
        }
    }

    fun setPlaylistType(type: PlaylistTypeItem?) {
        _playlistType.value = type?.value
    }
}
