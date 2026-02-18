import org.junit.Rule;
import org.junit.Test;
import static com.google.common.truth.Truth.assertWithMessage;
public abstract class TriangleTest {

    /** For autograding purposes; do not change this line. */
    abstract Triangle getNewTriangle();

    /* ***** TESTS ***** */

    // FIXME: Add additional tests for Triangle.java here that pass on a
    //  correct Triangle implementation and fail on buggy Triangle implementations.

    @Test
    public void testSidesFormTriangle() {
        // TODO: stub for first test
        Triangle t = getNewTriangle();
        // remember that you'll have to call on Triangle methods like
        // t.functionName(arguments), where t is a Triangle object
        assertWithMessage("Valid sides could not form triangle")
                .that(t.sidesFormTriangle(3, 4, 5)).isEqualTo(true);
    }

    @Test
    public void testSidesNotFormTriangle() {
        Triangle t = getNewTriangle();
        assertWithMessage("Sides could form triangle when sum of two sides is not larger than third side")
                .that(t.sidesFormTriangle(1, 2, 3)).isEqualTo(false);
    }

    @Test
    public void testSidesNotFormTriangleWithZeroInput() {
        Triangle t = getNewTriangle();
        assertWithMessage("Sides could form triangle with zero length side")
                .that(t.sidesFormTriangle(0, 1, 1)).isEqualTo(false);
    }

    @Test
    public void testSidesNotFormTriangleWithNegativeInput() {
        Triangle t = getNewTriangle();
        assertWithMessage("Sides could form triangle with negative length side")
                .that(t.sidesFormTriangle(-2, 2, 2)).isEqualTo(false);
    }

    @Test
    public void testPointsFormTriangle() {
        Triangle t = getNewTriangle();
        assertWithMessage("Valid points could not form triangle")
                .that(t.pointsFormTriangle(0, 0, 3, 0, 0, 3)).isEqualTo(true);
    }

    @Test
    public void testPointsNotFormTriangle() {
        Triangle t = getNewTriangle();
        assertWithMessage("Invalid points could form triangle")
                .that(t.pointsFormTriangle(2, 3, 3, 3, 4, 3)).isEqualTo(false);
    }

    @Test
    public void testPointsNotFormTriangleWithDuplicatedPoints() {
        Triangle t = getNewTriangle();
        assertWithMessage("Duplicated points could form triangle")
                .that(t.pointsFormTriangle(2, 3, 2, 3, 4, 5)).isEqualTo(false);
    }

    @Test
    public void testTriangleTypeScalene() {
        Triangle t = getNewTriangle();
        assertWithMessage("Triangle whose all sides are different lengths is not Scalene")
                .that(t.triangleType(3, 4, 5)).isEqualTo("Scalene");
    }

    @Test
    public void testTriangleTypeIsosceles() {
        Triangle t = getNewTriangle();
        assertWithMessage("Triangle whose two sides are different lengths is not Isosceles")
                .that(t.triangleType(2, 2, 3)).isEqualTo("Isosceles");
    }

    @Test
    public void testTriangleTypeEquilateral() {
        Triangle t = getNewTriangle();
        assertWithMessage("Triangle whose all sides are the same length is not Equilateral")
                .that(t.triangleType(2, 2, 2)).isEqualTo("Equilateral");
    }

    @Test
    public void testSquaredHypotenuse() {
        Triangle t = getNewTriangle();
        assertWithMessage("Squared hypotenuse of the triangle is wrong")
                .that(t.squaredHypotenuse(3, 4)).isEqualTo(25);
    }
}
