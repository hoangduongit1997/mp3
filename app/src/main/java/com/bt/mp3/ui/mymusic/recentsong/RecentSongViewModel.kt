package com.bt.mp3.ui.mymusic.recentsong

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.bt.base.extension.mapToCleanException
import com.bt.base.model.Result
import com.bt.base.ui.BaseViewModel
import com.bt.mp3.annotation.DefaultDispatcher
import com.bt.mp3.domain.usecase.GetRecentSongUseCase
import com.bt.mp3.model.SongItem
import com.bt.mp3.model.SongItemMapper
import kotlinx.coroutines.CoroutineDispatcher

class RecentSongViewModel @ViewModelInject constructor(
    private val getRecentSongUseCase: GetRecentSongUseCase,
    private val songItemMapper: SongItemMapper,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : BaseViewModel() {

    private val _recentSongs = MutableLiveData<List<SongItem>>()
    val recentSongsResult: LiveData<Result<List<SongItem>>> = liveData<Result<List<SongItem>>>(defaultDispatcher) {
        runCatching {
            setLoadingAsync(true)
            getRecentSongUseCase.execute().map {
                songItemMapper.mapToPresentation(it)
            }.run {
                emit(Result.Success(this))
                setLoadingAsync(false)
            }
        }.getOrElse {
            setExceptionAsync(it.mapToCleanException())
        }
    }
}
