import java.util.*;

public class AddingMachine {

	public static void main (String[] args) {

		Scanner scanner = new Scanner(System.in);
		boolean isPreviousZero = false;
		int total = 0;
		int subtotal = 0;
		int input;
		int MAXIMUM_NUMBER_OF_INPUTS = 100;
        int[] arrayOfInputs = new int[MAXIMUM_NUMBER_OF_INPUTS];
        int index = 0;
		int zeroIndex = 0;

        // TODO Add code anywhere below to complete AddingMachine
		while (true) {
			input = scanner.nextInt();
			if (index + zeroIndex == MAXIMUM_NUMBER_OF_INPUTS) {
				System.out.println("Input amount exceed maximum number of inputs");
				return;
			}
			if (input == 0) {
				if (isPreviousZero) {
					System.out.println("total " + total);
					for (int i = 0; i < index; i++) {
						System.out.println(arrayOfInputs[i]);
					}
					return;
				} else {
					System.out.println("subtotal " + subtotal);
					total += subtotal;
					subtotal = 0;
					isPreviousZero = true;
				}
				zeroIndex++;
			} else {
				subtotal += input;
				arrayOfInputs[index] = input;
				index++;
				isPreviousZero = false;
			}
		}
	}

}