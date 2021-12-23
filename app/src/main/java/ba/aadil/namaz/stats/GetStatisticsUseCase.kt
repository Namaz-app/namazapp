package ba.aadil.namaz.stats

import ba.aadil.namaz.db.TrackingDao

class GetStatisticsUseCase(trackingDao: TrackingDao) {
    fun getStatsForPastSevenDays(): String {
        return ""
    }
}