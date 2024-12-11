package com.suslovila.cybersus;

import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.IChatComponent;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {

    @Test
    void testAdd() {
        EntityPlayer player = new EntityPlayer() {
            @Override
            public void addChatMessage(IChatComponent iChatComponent) {

            }

            @Override
            public boolean canCommandSenderUseCommand(int i, String s) {
                return false;
            }

            @Override
            public ChunkCoordinates getCommandSenderPosition() {
                return null;
            }
        };
        assertEquals(5, result, "2 + 3 should equal 5");
    }

    @Test
    void testDivide() {
        assertEquals(5.0, result, "10 / 2 should equal 5.0");
    }

    @Test
    void testDivideByZero() {
        assertEquals("Division by zero is not allowed.", exception.getMessage());
    }
}