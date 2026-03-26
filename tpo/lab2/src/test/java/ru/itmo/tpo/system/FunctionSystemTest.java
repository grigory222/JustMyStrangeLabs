package ru.itmo.tpo.system;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.itmo.tpo.MathFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@DisplayName("FunctionSystem")
class FunctionSystemTest {

    private MathFunction trigMock;
    private MathFunction logMock;
    private FunctionSystem system;

    @BeforeEach
    void setUp() {
        trigMock = mock(MathFunction.class);
        logMock = mock(MathFunction.class);
        system = new FunctionSystem(trigMock, logMock);
    }

    // x < 0: тригонометрическая часть

    @DisplayName("x < 0: вызывается TrigPart")
    @Test
    void negativeUsesTrig() {
        when(trigMock.calculate(-1.0)).thenReturn(5.0);
        assertEquals(5.0, system.calculate(-1.0), 1e-9);
        verify(trigMock).calculate(-1.0);
        verifyNoInteractions(logMock);
    }

    // x = 0: граница, тригонометрическая часть

    @DisplayName("x = 0: вызывается TrigPart")
    @Test
    void zeroUsesTrig() {
        when(trigMock.calculate(0.0)).thenReturn(3.0);
        assertEquals(3.0, system.calculate(0.0), 1e-9);
        verify(trigMock).calculate(0.0);
        verifyNoInteractions(logMock);
    }

    // x > 0: логарифмическая часть

    @DisplayName("x > 0: вызывается LogPart")
    @Test
    void positiveUsesLog() {
        when(logMock.calculate(10.0)).thenReturn(18.5);
        assertEquals(18.5, system.calculate(10.0), 1e-9);
        verify(logMock).calculate(10.0);
        verifyNoInteractions(trigMock);
    }

    // Исключения из частей пробрасываются наружу

    @DisplayName("Исключение из TrigPart пробрасывается")
    @Test
    void trigExceptionPropagates() {
        when(trigMock.calculate(-1.0)).thenThrow(new ArithmeticException("tan = 0"));
        assertThrows(ArithmeticException.class, () -> system.calculate(-1.0));
    }

    @DisplayName("Исключение из LogPart пробрасывается")
    @Test
    void logExceptionPropagates() {
        when(logMock.calculate(1.0)).thenThrow(new ArithmeticException("log10 = 0"));
        assertThrows(ArithmeticException.class, () -> system.calculate(1.0));
    }
}
