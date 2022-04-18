package ba.aadil.namaz.util

import java.time.LocalDate
import java.time.ZoneOffset

fun LocalDate.toInstant() = this.atStartOfDay().toInstant(ZoneOffset.UTC)