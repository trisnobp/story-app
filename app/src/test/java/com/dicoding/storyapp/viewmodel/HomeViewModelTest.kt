package com.dicoding.storyapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.dicoding.storyapp.data.StoryRepository
import com.dicoding.storyapp.data.remote.response.ListStoryItem
import com.dicoding.storyapp.utils.DummyData
import com.dicoding.storyapp.utils.MainDispatcherRule
import com.dicoding.storyapp.utils.getOrAwaitValue
import com.dicoding.storyapp.view.utils.StoryAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {

    @Mock
    private lateinit var storyRepository: StoryRepository

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @Test
    fun `when Data Is Successfully Loaded, Should Not Be Null and Return Data`() = runTest {
        val pagingStoryData = DummyData.generatePagingData()
        val data: PagingData<ListStoryItem> = StoryPagingSource.snapshot(pagingStoryData)
        val expectedStory = MutableLiveData<PagingData<ListStoryItem>>()
        expectedStory.value = data

        Mockito.`when`(storyRepository.getStoriesInPaging(TOKEN)).thenReturn(expectedStory)

        val homeViewModel = HomeViewModel(storyRepository)
        val actualpagingStoryData: PagingData<ListStoryItem> = homeViewModel.getStoriesInPaging(
            TOKEN).getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualpagingStoryData)

        assertNotNull(differ.snapshot())
        assertEquals(pagingStoryData.size, differ.snapshot().size)
        assertEquals(pagingStoryData[0], differ.snapshot()[0])
    }

    @Test
    fun `when Data Is Empty, Should Return No Data`() = runTest {
        val data: PagingData<ListStoryItem> = PagingData.from(emptyList())
        val expectedPagingData = MutableLiveData<PagingData<ListStoryItem>>()
        expectedPagingData.value = data

        Mockito.`when`(storyRepository.getStoriesInPaging(TOKEN)).thenReturn(expectedPagingData)

        val homeViewModel = HomeViewModel(storyRepository)
        val actualPagingStoryData: PagingData<ListStoryItem> = homeViewModel.getStoriesInPaging(TOKEN).getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualPagingStoryData)
        assertEquals(0, differ.snapshot().size)
    }

    companion object {
        private const val TOKEN: String = "token"
    }
}

class StoryPagingSource : PagingSource<Int, LiveData<List<ListStoryItem>>>() {
    companion object {
        fun snapshot(items: List<ListStoryItem>): PagingData<ListStoryItem> {
            return PagingData.from(items)
        }
    }
    override fun getRefreshKey(state: PagingState<Int, LiveData<List<ListStoryItem>>>): Int {
        return 0
    }
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<ListStoryItem>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }
}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}
