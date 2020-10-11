package sword_offer;

//给定一个double类型的浮点数base和int类型的整数exponent。求base的exponent次方。
//保证base和exponent不同时为0
public class JZ12 {
    public double Power(double base, int exponent) {
        double result = 1;
        for (int i = 0; i < Math.abs(exponent); i++) {
            result *= base;
        }
        return exponent > 0 ? result : 1 / result;
    }

    public static void main(String[] args) {
        System.out.println(new JZ12().Power(2, -3));
    }
}
