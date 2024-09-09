package com.cyber.restory.domain.usecase

import com.cyber.restory.data.model.PostRequest
import com.cyber.restory.data.model.PostResponse
import com.cyber.restory.domain.repository.PostRepository
import javax.inject.Inject

class GetPostsUseCase @Inject constructor(
    private val postRepository: PostRepository
) {
    suspend operator fun invoke(type: String, size: Int, page: Int): PostResponse {
        return postRepository.getPosts(type, size, page)
    }
}