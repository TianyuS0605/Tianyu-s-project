import org.junit.Test;
import static com.google.common.truth.Truth.assertWithMessage;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class BinaryTreeTest {
    @Test
    public void treeFormatTest() {
        BinarySearchTree<String> x = new BinarySearchTree<String>();
        x.add("C");
        x.add("A");
        x.add("E");
        x.add("B");
        x.add("D");
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream oldOut = System.out;
        System.setOut(new PrintStream(outContent));
        BinaryTree.print(x, "x");
        System.setOut(oldOut);
        assertWithMessage("The preorder and/or inorder traversals for the given BinarySearchTree are incorrect")
                .that(outContent.toString().trim())
                .isEqualTo("x in preorder\nC A B E D \nx in inorder\nA B C D E \n\n".trim());

        assertWithMessage("Tree not contains B").that(x.contains("B")).isEqualTo(true);
        assertWithMessage("Tree contains F").that(x.contains("F")).isEqualTo(false);
        x.delete("B");
        assertWithMessage("Tree contains B").that(x.contains("B")).isEqualTo(false);

        BinarySearchTree<Integer> y = new BinarySearchTree<Integer>();
        y.add(1);
        y.add(3);
        y.add(2);
        assertWithMessage("Tree not contains 2").that(y.contains(2)).isEqualTo(true);
        y.add(2);
        y.delete(2);
        assertWithMessage("Tree not contains 2").that(y.contains(2)).isEqualTo(false);

        /* if the above test isn't working due to operating system differences, try commenting out the above line
        and replace it with the following:

       assertWithMessage("The preorder and/or inorder traversals for the given BinarySearchTree are incorrect")
                .that(outContent.toString().trim())
                .isEqualTo("x in preorder" + System.lineSeparator() + "C A B E D " + System.lineSeparator() + "x in inorder"
                        + System.lineSeparator() + "A B C D E ");

        */
    }
}