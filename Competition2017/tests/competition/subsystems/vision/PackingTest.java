package competition.subsystems.vision;

import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

import competition.BaseTest;

public class PackingTest extends BaseTest {
    @Test
    public void testWheelOdomSmallPositiveValues() {
       byte[] result = VisionSubsystem.packWheelOdomFrame(5, 7, 2);
       
       assertArrayEquals(new byte[] {
           // 5 * 1,000
           (byte)0b0001_0011,
           (byte)0b1000_1000,
           // 7 * 1,000
           (byte)0b0001_1011,
           (byte)0b0101_1000,
           // 2 * 10,000
           (byte)0b0100_1110,
           (byte)0b0010_0000
       }, result);
    }
    
    @Test
    public void testWheelOdomFractionalPositiveValues() {
       byte[] result = VisionSubsystem.packWheelOdomFrame(0.5, 0.07, 0.002);
       
       assertArrayEquals(new byte[] {
           // 0.5 * 1,000
           (byte)0b0000_0001,
           (byte)0b1111_0100,
           // 0.7 * 1,000
           (byte)0b0000_0000,
           (byte)0b0100_0110,
           // 0.002 * 10,000
           (byte)0b0000_0000,
           (byte)0b0001_0100
       }, result);
    }
    
    @Test
    public void testWheelOdomSmallNegativeValues() {
       byte[] result = VisionSubsystem.packWheelOdomFrame(-5, -7, 2);
       
       assertArrayEquals(new byte[] {
           // -5 * 1,000 (two's complement)
           (byte)0b1110_1100,
           (byte)0b0111_1000,
           // -7 * 1,000  (two's complement)
           (byte)0b1110_0100,
           (byte)0b1010_1000,
           // 2 * 10,000
           (byte)0b0100_1110,
           (byte)0b0010_0000
       }, result);
    }
     
}
