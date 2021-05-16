package pw.forst.katlib

import org.junit.jupiter.api.Test
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date
import kotlin.streams.toList
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

internal class DateExtensionsTest {

    @Test
    fun testGetDateRangeTo() {
        val from = LocalDate.of(2018, 8, 10)
        val toTight = LocalDate.of(2018, 8, 10)
        val daysTight = listOf(LocalDate.of(2018, 8, 10))
        assertEquals(daysTight, from.getDateRangeTo(toTight))

        val toLoose = LocalDate.of(2018, 8, 11)
        val daysLoose = listOf(LocalDate.of(2018, 8, 10), LocalDate.of(2018, 8, 11))
        assertEquals(daysLoose, from.getDateRangeTo(toLoose))

        val toInfeasible = LocalDate.of(2018, 8, 9)
        val daysInfeasible = listOf<LocalDate>()
        assertEquals(daysInfeasible, from.getDateRangeTo(toInfeasible))
    }

    @Test
    fun testGetDateRangeToAsStream() {
        val from = LocalDate.of(2018, 8, 10)
        val toTight = LocalDate.of(2018, 8, 10)
        val daysTight = listOf(LocalDate.of(2018, 8, 10))
        assertEquals(daysTight, from.getDateRangeToAsStream(toTight).toList())

        val toLoose = LocalDate.of(2018, 8, 11)
        val daysLoose = listOf(LocalDate.of(2018, 8, 10), LocalDate.of(2018, 8, 11))
        assertEquals(daysLoose, from.getDateRangeToAsStream(toLoose).toList())

        val toInfeasible = LocalDate.of(2018, 8, 9)
        val daysInfeasible = listOf<LocalDate>()
        assertEquals(daysInfeasible, from.getDateRangeToAsStream(toInfeasible).toList())
    }

    @Test
    fun testGetInvertedDateRangeToAsStream() {
        val from = LocalDate.of(2018, 8, 10)
        val toTight = LocalDate.of(2018, 8, 10)
        val daysTight = listOf(LocalDate.of(2018, 8, 10))
        assertEquals(daysTight, from.getInvertedDateRangeToAsStream(toTight).toList())

        val toLoose = LocalDate.of(2018, 8, 9)
        val daysLoose = listOf(LocalDate.of(2018, 8, 10), LocalDate.of(2018, 8, 9))
        assertEquals(daysLoose, from.getInvertedDateRangeToAsStream(toLoose).toList())

        val toInfeasible = LocalDate.of(2018, 8, 11)
        val daysInfeasible = listOf<LocalDate>()
        assertEquals(daysInfeasible, from.getInvertedDateRangeToAsStream(toInfeasible).toList())
    }

    @Test
    fun testGetDaysInInterval() {
        val from = LocalDate.of(2018, 8, 10)
        val toTight = LocalDate.of(2018, 8, 10)
        assertEquals(1, from.getDaysInInterval(toTight))

        val toLoose = LocalDate.of(2018, 8, 11)
        assertEquals(2, from.getDaysInInterval(toLoose))

        val toInfeasible = LocalDate.of(2018, 8, 9)
        assertFailsWith<IllegalArgumentException> { from.getDaysInInterval(toInfeasible) }
    }

    @Test
    fun testGetDayDifference() {
        val from = LocalDate.of(2018, 8, 10)
        val toTight = LocalDate.of(2018, 8, 10)
        assertEquals(0, from.getDayDifference(toTight))

        val toLoose = LocalDate.of(2018, 8, 11)
        assertEquals(1, from.getDayDifference(toLoose))

        val toNegative = LocalDate.of(2018, 8, 9)
        assertEquals(-1, from.getDayDifference(toNegative))
    }

    @Test
    fun testGetWeekOfYear() {
        val sunday = LocalDate.of(2018, 1, 7)
        assertEquals(1, sunday.getWeekOfYear())
        val monday = LocalDate.of(2018, 1, 8)
        assertEquals(2, monday.getWeekOfYear())
    }

    @Test
    fun testSetWeekOfYearMonday() {
        val year = LocalDate.ofYearDay(2022, 100)
        val week0Date = LocalDate.of(2021, 12, 27)
        assertEquals(week0Date, year.setWeekOfYearMonday(0))

        val week1Date = LocalDate.of(2022, 1, 3)
        assertEquals(week1Date, year.setWeekOfYearMonday(1))

        val week2Date = LocalDate.of(2022, 1, 10)
        assertEquals(week2Date, year.setWeekOfYearMonday(2))


        val week52Date = LocalDate.of(2022, 12, 26)
        assertEquals(week52Date, year.setWeekOfYearMonday(52))

        val week53Date = LocalDate.of(2023, 1, 2)
        assertEquals(week53Date, year.setWeekOfYearMonday(53))
    }

    @Test
    fun `test from Date to LocalDate`() {
        val instant = Instant.ofEpochMilli(1_000_000)
        val date = Date.from(instant)
        val zone = ZoneId.systemDefault()
        // Use current system time zone
        val expectedLocalDate = LocalDate.from(instant.atZone(zone))

        val actualLocalDate = date.toLocalDate(zone)

        assertEquals(expectedLocalDate, actualLocalDate)
    }

    @Test
    fun `test from Date to LocalDate in UTC`() {
        val instant = Instant.ofEpochMilli(1_000_000)
        val date = Date.from(instant)
        val zone = ZoneId.of("UTC")
        // Use current system time zone
        val expectedLocalDate = LocalDate.from(instant.atZone(zone))

        assertEquals(expectedLocalDate, date.toUtcLocalDate())
    }

}
