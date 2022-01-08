package ba.aadil.namaz.motivation

import ba.aadil.namaz.R

class GetEmojiAndCongratsForPrayedPrayers {
    fun get(prayedCount: Int): Pair<Int, String> {
        return when (prayedCount) {
            0 -> Pair(R.string.congrats_gods_blessings3, "\uD83D\uDE1E")
            1, 2 -> Pair(R.string.congrats_gods_blessings2, "\uD83D\uDE10")
            3, 4 -> Pair(R.string.congrats_gods_blessings, "☺️")
            else -> Pair(R.string.congrats_gods_blessings, "\uD83E\uDD29")
        }
    }
}