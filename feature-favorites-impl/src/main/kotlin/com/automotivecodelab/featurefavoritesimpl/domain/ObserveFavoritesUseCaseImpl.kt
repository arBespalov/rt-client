package com.automotivecodelab.featurefavoritesimpl.domain

import com.automotivecodelab.featurefavoritesapi.Favorite
import com.automotivecodelab.featurefavoritesapi.ObserveFavoritesUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveFavoritesUseCaseImpl @Inject constructor(
    private val favoritesRepository: FavoritesRepository
): ObserveFavoritesUseCase {
    override operator fun invoke(): Flow<List<Favorite>> {
        return favoritesRepository.observeFavorites()
    }
}