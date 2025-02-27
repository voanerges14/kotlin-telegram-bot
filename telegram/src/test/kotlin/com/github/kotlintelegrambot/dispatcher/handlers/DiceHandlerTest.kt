package com.github.kotlintelegrambot.dispatcher.handlers

import anyDice
import anyMessage
import anyUpdate
import com.github.kotlintelegrambot.Bot
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.jupiter.api.Test

class DiceHandlerTest {

    private val handleDiceMock = mockk<HandleDice>(relaxed = true)

    private val sut = DiceHandler(handleDiceMock)

    @Test
    fun `checkUpdate returns false when there is no message`() {
        val anyUpdateWithNoMessage = anyUpdate(message = null)

        val checkUpdateResult = sut.checkUpdate(anyUpdateWithNoMessage)

        assertFalse(checkUpdateResult)
    }

    @Test
    fun `checkUpdate returns false when there is no dice`() {
        val anyUpdateWithNoDice = anyUpdate(message = anyMessage(dice = null))

        val checkUpdateResult = sut.checkUpdate(anyUpdateWithNoDice)

        assertFalse(checkUpdateResult)
    }

    @Test
    fun `checkUpdate returns true when there is dice`() {
        val anyUpdateWithDice = anyUpdate(message = anyMessage(dice = anyDice()))

        val checkUpdateResult = sut.checkUpdate(anyUpdateWithDice)

        assertTrue(checkUpdateResult)
    }

    @Test
    fun `dice is properly dispatched to the handler function`() {
        val botMock = mockk<Bot>()
        val anyDice = anyDice()
        val anyMessageWithDice = anyMessage(dice = anyDice)
        val anyUpdateWithDice = anyUpdate(message = anyMessageWithDice)

        sut.handlerCallback(botMock, anyUpdateWithDice)

        val expectedDiceHandlerEnv = DiceHandlerEnvironment(
            botMock,
            anyUpdateWithDice,
            anyMessageWithDice,
            anyDice
        )
        verify { handleDiceMock.invoke(expectedDiceHandlerEnv) }
    }
}
