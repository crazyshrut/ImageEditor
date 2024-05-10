
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;

class imageEditor {

    public static BufferedImage pixelatedBlur(BufferedImage inputImage, int strength) throws Exception {
        int height = inputImage.getHeight();
        int width = inputImage.getWidth();
        if (strength > height || strength > width) {
            throw new Exception("Invalid Strength!");
        }
        BufferedImage outputImage = new BufferedImage(width, height,
                BufferedImage.TYPE_3BYTE_BGR);
        for (int i = 0; i < height / strength; i++) {
            for (int j = 0; j < width / strength; j++) {
                long red = 0;
                long blue = 0;
                long green = 0;
                for (int k = 0; k < strength; k++) {
                    for (int l = 0; l < strength; l++) {
                        Color color = new Color(inputImage.getRGB(strength * j + l, strength * i + k));
                        red += color.getRed();
                        blue += color.getBlue();
                        green += color.getGreen();
                    }
                }
                int averageRed = (int) (red / (strength * strength));
                int averageBlue = (int) (blue / (strength * strength));
                int averageGreen = (int) (green / (strength * strength));
                Color averageColor = new Color(averageRed, averageGreen, averageBlue);
                for (int k = 0; k < strength; k++) {
                    for (int l = 0; l < strength; l++) {
                        outputImage.setRGB(strength * j + l, strength * i + k, averageColor.getRGB());
                    }
                }
            }
        }
        return outputImage;
    }

    public static BufferedImage rotateImageClockwise(BufferedImage inputImage) {
        int height = inputImage.getWidth();
        int width = inputImage.getHeight();
        BufferedImage outputImage = new BufferedImage(width, height,
                BufferedImage.TYPE_3BYTE_BGR);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                outputImage.setRGB(j, i, inputImage.getRGB(i, width - 1 - j));
            }
        }
        return outputImage;
    }

    public static BufferedImage rotateImageAntiClockwise(BufferedImage inputImage) {
        BufferedImage outputImage = rotateImageClockwise(inputImage);
        outputImage = rotateImageClockwise(outputImage);
        outputImage = rotateImageClockwise(outputImage);
        return outputImage;
    }

    public static BufferedImage convertToGrayScale(BufferedImage inputImage) {
        int height = inputImage.getHeight();
        int width = inputImage.getWidth();
        BufferedImage outputImage = new BufferedImage(width, height,
                BufferedImage.TYPE_BYTE_GRAY);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                outputImage.setRGB(j, i, inputImage.getRGB(j, i));
            }
        }
        return outputImage;
    }

    public static BufferedImage horizontalInvert(BufferedImage inputImage) {
        int height = inputImage.getHeight();
        int width = inputImage.getWidth();
        BufferedImage outputImage = new BufferedImage(width, height,
                BufferedImage.TYPE_3BYTE_BGR);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                outputImage.setRGB(j, i, inputImage.getRGB(width - j - 1, i));
            }
        }
        return outputImage;
    }

    public static BufferedImage verticalInvert(BufferedImage inputImage) {
        int height = inputImage.getHeight();
        int width = inputImage.getWidth();
        BufferedImage outputImage = new BufferedImage(width, height,
                BufferedImage.TYPE_3BYTE_BGR);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                outputImage.setRGB(j, i, inputImage.getRGB(j, height - i - 1));
            }
        }
        return outputImage;
    }

    public static int updateValue(int n) {
        int increased = n + n / 5;
        if (increased <= 255)
            return increased;
        return 255;
    }

    public static BufferedImage increaseBrightness(BufferedImage inputImage) {
        int height = inputImage.getHeight();
        int width = inputImage.getWidth();
        BufferedImage outputImage = new BufferedImage(width, height,
                BufferedImage.TYPE_3BYTE_BGR);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Color pixel = new Color(inputImage.getRGB(j, i));
                int red = updateValue(pixel.getRed());
                int blue = updateValue(pixel.getBlue());
                int green = updateValue(pixel.getGreen());
                Color newPixel = new Color(red, green, blue);
                outputImage.setRGB(j, i, newPixel.getRGB());
            }
        }
        return outputImage;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter filename to edit (must be in the same folder): ");
        String inputFilePath = scanner.nextLine();
        File inputFile = new File(inputFilePath);
        try {
            BufferedImage inputImage = ImageIO.read(inputFile);
            while (true) {
                System.out.println();
                System.out.println("===== SELECT A FILTER =====");
                System.out.println("1: grayscaled");
                System.out.println("2: horizontal flip");
                System.out.println("3: vertical flip");
                System.out.println("4: rotate clockwise");
                System.out.println("5: rotate anticlockwise");
                System.out.println("6: increase brightness");
                System.out.println("7: blur");
                System.out.println("q: QUIT");
                System.out.print("\nEnter an option: ");
                String choice = scanner.next();
                BufferedImage filtered;
                if (choice.equals("1")) {
                    filtered = convertToGrayScale(inputImage);
                } else if (choice.equals("2")) {
                    filtered = horizontalInvert(inputImage);
                } else if (choice.equals("3")) {
                    filtered = verticalInvert(inputImage);
                } else if (choice.equals("4")) {
                    filtered = rotateImageClockwise(inputImage);
                } else if (choice.equals("5")) {
                    filtered = rotateImageAntiClockwise(inputImage);
                } else if (choice.equals("6")) {
                    filtered = increaseBrightness(inputImage);
                } else if (choice.equals("7")) {
                    System.out.print("Enter blur strength (<= width && height): ");
                    int strength = scanner.nextInt();
                    try {
                        filtered = pixelatedBlur(inputImage, strength);
                    } catch (Exception e) {
                        System.out.println("Invalid Strength!");
                        continue;
                    }
                } else if (choice.equals("q")) {
                    System.out.println("BYE");
                    break;
                } else {
                    System.out.println("Select a valid option");
                    continue;
                }
                System.out.print("Enter the name for output file: ");
                String outFileName = scanner.next();
                File outputFile = new File(outFileName + ".jpg");
                boolean didWrite = ImageIO.write(filtered, "jpg", outputFile);
                System.out.println(didWrite ? "\nEdit Successfull!" : "\nEdit Failed!");
            }
        } catch (IOException a) {
            System.out.println("Invalid input file!");
        }
        scanner.close();
    }
}