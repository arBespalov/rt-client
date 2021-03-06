package com.automotivecodelab.featurefavoritesimpl.data

import com.automotivecodelab.featurefavoritesapi.Favorite
import com.automotivecodelab.featurefavoritesimpl.domain.FavoritesRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Singleton
class FavoritesRepositoryImpl @Inject constructor(
    private val favoritesDao: FavoritesDao
) : FavoritesRepository {
    override fun observeFavorites(): Flow<List<Favorite>> {
        return favoritesDao.observeFavorites()
            .map { list ->
                list.map { dbModel -> dbModel.toDomainModel() }
            }
    }

    override suspend fun addToFavorites(favorite: Favorite) {
        favoritesDao.insertFavorite(favorite.toDbModel())
    }

    override suspend fun removeFromFavorites(favorite: Favorite) {
        favoritesDao.deleteFavorite(favorite.toDbModel())
    }
}
