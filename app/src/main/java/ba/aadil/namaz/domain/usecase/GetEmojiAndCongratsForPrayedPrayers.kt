package ba.aadil.namaz.domain.usecase

import ba.aadil.namaz.R

class GetEmojiAndCongratsForPrayedPrayers {
    fun get(prayedCount: Int): ResultData {
        return when (prayedCount) {
            0 -> ResultData(R.string.congrats_gods_blessings3, "\uD83D\uDE1E")
            1, 2 -> ResultData(R.string.congrats_gods_blessings2, "\uD83D\uDE10")
            3, 4 -> ResultData(R.string.congrats_gods_blessings, "☺️")
            else -> ResultData(R.string.congrats_gods_blessings, "\uD83E\uDD29")
        }
    }

    data class ResultData(val congratsTextId: Int, val emoji: String)
}