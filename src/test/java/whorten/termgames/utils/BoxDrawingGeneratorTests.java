package whorten.termgames.utils;

import static org.junit.Assert.*;

import org.junit.Test;

public class BoxDrawingGeneratorTests {

    @Test
    public void Transform_GivenInput_ReturnsNotNull()
    {
        //Arrange
    	BoxDrawingGenerator sut = new BoxDrawingGenerator();
        String[] test = {"  ",
                         "  "};

        //Act
        String[] result = sut.transform(test);

        //Assert
        assertNotNull(result);
    }

    @Test
    public void Transform_GivenHorizontalSection_ReturnsBar()
    {
        //Arrange
    	BoxDrawingGenerator sut = new BoxDrawingGenerator();
        String[] test = {"##"};
        String[] expected = { "══" };

        //Act
        String[] result = sut.transform(test);

        //Assert
        assertEquals(expected[0], result[0]);
    }

    @Test
    public void Transform_GivenVerticalSection_ReturnsBar()
    {
        //Arrange
    	BoxDrawingGenerator sut = new BoxDrawingGenerator();
        String[] test = { "#", "#" };
        String expected = "║" ;

        //Act
        String[] result = sut.transform(test);

        //Assert
        assertEquals(expected, result[0]);
    }

    @Test
    public void Transform_GivenTopLeftCornerSection_ReturnsCorner()
    {
        //Arrange
    	BoxDrawingGenerator sut = new BoxDrawingGenerator();
        String[] test = { "##",
                          "# "};
        char expected = '╔';

        //Act
        String[] result = sut.transform(test);

        //Assert
        assertEquals(expected, result[0].charAt(0));
    }

    @Test
    public void Transform_GivenTopRightCornerSection_ReturnsCorner()
    {
        //Arrange
    	BoxDrawingGenerator sut = new BoxDrawingGenerator();
        String[] test = { "##",
                          " #"};
        char expected = '╗';

        //Act
        String[] result = sut.transform(test);

        //Assert
        assertEquals(expected, result[0].charAt(1));
    }

    @Test
    public void Transform_GivenBottomRightCornerSection_ReturnsCorner()
    {
        //Arrange
    	BoxDrawingGenerator sut = new BoxDrawingGenerator();
        String[] test = { " #",
                          "##"};
        char expected = '╝';

        //Act
        String[] result = sut.transform(test);

        //Assert
        assertEquals(expected, result[1].charAt(1));
    }

    @Test
    public void Transform_GivenBottomLeftCornerSection_ReturnsCorner()
    {
        //Arrange
    	BoxDrawingGenerator sut = new BoxDrawingGenerator();
        String[] test = { "# ",
                          "##"};
        char expected = '╚';

        //Act
        String[] result = sut.transform(test);

        //Assert
        assertEquals(expected, result[1].charAt(0));
    }

    @Test
    public void Transform_GivenVerticalRightTeeSection_ReturnsTee()
    {
        //Arrange
    	BoxDrawingGenerator sut = new BoxDrawingGenerator();
        String[] test = { "# ",
                          "##",
                          "# "};
        char expected = '╠';

        //Act
        String[] result = sut.transform(test);

        //Assert
        assertEquals(expected, result[1].charAt(0));
    }

    @Test
    public void Transform_GivenVerticalLeftTeeSection_ReturnsTee()
    {
        //Arrange
    	BoxDrawingGenerator sut = new BoxDrawingGenerator();
        String[] test = { " # ",
                          "## ",
                          " # "};
        char expected = '╣';

        //Act
        String[] result = sut.transform(test);

        //Assert
        assertEquals(expected, result[1].charAt(1));
    }
}
