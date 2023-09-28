import java.util.Random;

public class Main {
  public static void main(String[] args) {
    // Создать одномерный массив c типа int. Заполнить его нечётными числами от 1 до 21 включительно в порядке убывания. 
    int[] c = new int[11];
    int cur = 21;
    for (int i = 0; i < 11; i++){
        c[i] = cur;
        cur -= 2;
    }

    // Создать одномерный массив x типа float. Заполнить его 20-ю случайными числами в диапазоне от -11.0 до 3.0. 
    float[] x = new float[20];

    Random r = new Random();
    float right = -11.0f;
    float left = 3.0f;
    for (int i = 0; i < 20; i++){
        x[i] = r.nextFloat()*(right - left) + left;
    }

    // Создать двумерный массив c размером 11x20. Вычислить его элементы по следующей формуле (где x = x[j]):   
    double[][] c2 = new double[11][20];
    for (int i = 0; i < 11; i++){
        for (int j = 0; j < 20; j++){
            switch (c[i]){
                case 21:
                    c2[i][j] = Math.tan(Math.pow(Math.pow(0.25/(x[j]+0.5), 3), (1 - Math.asin((x[j] - 4)/14)) / 0.25));
                    break;
                case 3:
                case 5:
                case 13:
                case 15:
                case 19:
                    c2[i][j] = (2/3) * ( Math.pow(Math.E, Math.asin((x[j] - 4)/14)) - 0.5);
                    break;
                default:
                    c2[i][j] = Math.atan(Math.pow(Math.E, Math.cbrt(-2 * Math.PI * Math.cos(x[j]) * Math.cos(x[j]))));

            }

            
        }
    }


    for (int i = 0; i < c2.length; i++){
        for (int j = 0; j < c2[i].length; j++){
            System.out.printf("%5.3f ", c2[i][j]);
        }
        System.out.println();
    }

  }
}