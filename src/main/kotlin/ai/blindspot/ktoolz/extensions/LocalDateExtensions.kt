package ai.blindspot.ktoolz.extensions

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAdjusters
import java.time.temporal.WeekFields
import java.util.Locale
import java.util.stream.Stream
import kotlin.streams.toList


/**
 * Returns list of days from [this] date to [to] date (both inclusive).
 */
fun LocalDate.getDateRangeTo(to: LocalDate): List<LocalDate> = this.getDateRangeToAsStream(to).toList()

/**
 * Returns stream of days from [this] date to [to] date (both inclusive).
 */
fun LocalDate.getDateRangeToAsStream(to: LocalDate): Stream<LocalDate> = Stream.iterate(this) { d -> d.plusDays(1) }
    .limit(this.until(to, ChronoUnit.DAYS) + 1)

/**
 * Returns stream of decreasing days from [this] date to [to] date (both inclusive). This means that [this] date should be later than [to] date
 * to obtain non-empty stream.
 */
fun LocalDate.getInvertedDateRangeToAsStream(to: LocalDate): Stream<LocalDate> = Stream.iterate(this) { d -> d.minusDays(1) }
    .limit(to.until(this, ChronoUnit.DAYS) + 1)


/**
 * Returns week of year for [this]
 */
fun LocalDate.getWeekOfYear(): Int = this.get(WeekFields.of(Locale.GERMANY).weekOfYear())

/**
 * Returns [this] LocalDate with week of the year set to [week] and day of the week set to [DayOfWeek.MONDAY]. I.e. when [this] date is 23-08-2019 and [week]
 * is 2, the returned date is 07-01-2019.
 */
fun LocalDate.setWeekOfYearMonday(week: Int): LocalDate = this
    .with(WeekFields.of(Locale.GERMANY).weekOfYear(), week.toLong())
    .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))

/**
 * Returns number of days in interval between [this] date and [to] date (both inclusive). This means that this method returns 1 when [this] and [to] are equal.
 */
fun LocalDate.getDaysInInterval(to: LocalDate): Int = (ChronoUnit.DAYS.between(this, to).toInt() + 1)
    .takeIf { it > 0 } ?: throw IllegalArgumentException("start date $this is smaller than end date $to.")

/**
 * Returns number of days between [this] date and [to] date (exclusive). This means that this method returns 0 when [this] and [to] are equal.
 */
fun LocalDate.getDayDifference(to: LocalDate) = ChronoUnit.DAYS.between(this, to).toInt()
