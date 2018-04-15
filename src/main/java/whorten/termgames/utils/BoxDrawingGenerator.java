package whorten.termgames.utils;

public class BoxDrawingGenerator {

    public String[] transform(String[] input)
    {
            //make array of arrays
        char[][] inputBuffer = new char[input.length][];
        for(int i = 0; i < input.length; i++)
        {
            inputBuffer[i] = input[i].toCharArray();
        }


        String[] output = new String[input.length];
        for(int row = 0; row < input.length; row++)
        {
            StringBuilder sb = new StringBuilder();
            for(int col = 0; col < input[row].length(); col++)
            {
                //if character isn't a wall, skip
                if(inputBuffer[row][col] != '#')
                {
                    sb.append(' ');
                    continue;
                }

                //create signature to apply rules
                String signature = GetSignature(row, col, inputBuffer);

                //edge cases
                switch (signature)
                {
                    // vertical
                    //  |###| |###| |###| | ##| |## | |###| |## | | ##|
                    //  |## | | ##| | ##| | ##| |## | |## | |## | | ##|
                    //  |###| |###| | ##| |###| |###| |## | |## | | ##|
                    case "##### ###":
                    case "### #####":
                    case "### ## ##":
                    case " ## #####":
                    case "## ## ###":
                    case "##### ## ":
                    case " ## ## ##":
                    case "## ## ## ":
                        sb.append('║');
                        continue;
                    // horizontal
                    //  |###| |# #|  |###| |  #|  |###| |#  | |   | |###|
                    //  |###| |###|  |###| |###|  |###| |###| |###| |###|
                    //  |# #| |###|  |  #| |###|  |#  | |###| |###| |   |
                    case "####### #":
                    case "# #######":
                    case "######  #":
                    case "  #######":
                    case "#######  ":
                    case "#  ######":
                    case "   ######":
                    case "######   ":
                        sb.append('═');
                        continue;
                    // top left
                    //  |###|                        
                    //  |###|
                    //  |## |
                    case "######## ":
                        sb.append('╔');
                        continue;
                    // top right
                    //  |###| 
                    //  |###| 
                    //  | ##| 
                    case "###### ##":
                        sb.append('╗');
                        continue;
                    // bottom left
                    //  |## |
                    //  |###|
                    //  |###|
                    case "## ######":
                        sb.append('╚');
                        continue;
                    // bottom right
                    //  | ##|     
                    //  |###|     
                    //  |###|     
                    case " ########":
                        sb.append('╝');
                        continue;
                    // vertical right tee
                    //  |## |                        
                    //  |###|
                    //  |## |
                    case "## ##### ":
                        sb.append('╠');
                        continue;
                    // vertical left tee
                    //  | ##|                        
                    //  |###|
                    //  | ##|
                    case " ##### ##":
                        sb.append('╣');
                        continue;
                    // horizontal down tee
                    //  |###|                        
                    //  |###|
                    //  | # |
                    case "###### # ":
                        sb.append('╦');
                        continue;
                    // horizontal up tee
                    //  | # |                        
                    //  |###|
                    //  |###|
                    case " # ######":
                        sb.append('╩');
                        continue;
                    //  cross           
                    //  | # |  |## |  | ##|  | # |  | # |  | ##|  |## |                     
                    //  |###|  |###|  |###|  |###|  |###|  |###|  |###|
                    //  | # |  | # |  | # |  |## |  | ##|  |## |  | ##|
                    case " # ### # ":
                    case "## ### # ":
                    case " ##### # ":
                    case " # ##### ":
                    case " # ### ##":
                    case " ####### ":
                    case "## ### ##":
                        sb.append('╬');
                        continue;
                    default:
                        break;
                }


                //catch main cases first
                //horizontal
                if (signature.charAt(1) == ' ' && signature.charAt(7) == ' ')
                {
                    sb.append('═');
                    continue;
                }

                //vertical
                if (signature.charAt(3) == ' ' && signature.charAt(5) == ' ')
                {
                    sb.append('║');
                    continue;
                }

                //top-left corner or vertical-right tee
                //  |X?X|
                //  | ##|
                //  |X#X|
                if (signature.charAt(3) == ' ' && signature.charAt(5) == '#' && signature.charAt(7) == '#')
                {
                    if(signature.charAt(1) == ' ')
                    {
                        sb.append('╔');
                    } else
                    {
                        sb.append('╠');
                    }
                    
                    continue;
                }

                //top-right corner or horizontal down tee
                //  |X X|
                //  |##?|
                //  |X#X|
                if (signature.charAt(3) == '#' && signature.charAt(1) == ' ' && signature.charAt(7) == '#')
                {
                    if(signature.charAt(5) == ' ')
                    {
                        sb.append('╗');
                    } else
                    {
                        sb.append('╦');
                    }
                    continue;
                }

                //bottom-left corner or horizontal up tee
                //  |X#X|
                //  |?##|
                //  |X X|
                if (signature.charAt(1) == '#' && signature.charAt(5) == '#' && signature.charAt(7) == ' ')
                {
                    if (signature.charAt(3) == ' ')
                    {
                        sb.append('╚');
                    }
                    else
                    {
                        sb.append('╩');
                    }
                    continue;
                }

                //botton-right corner  or vertical-left tee
                //  |X#X|
                //  |## |
                //  |X?X|
                if (signature.charAt(1) == '#' && signature.charAt(3) == '#' && signature.charAt(5) == ' ')
                {
                    if (signature.charAt(7) == ' ')
                    {
                        sb.append('╝');
                    }
                    else
                    {
                        sb.append('╣');
                    }
                    continue;
                }

                //if nothing else has gone, just append a space
                sb.append(' ');

            }
            output[row] = sb.toString();
        }
        return output;
    }

    private String GetSignature(int row, int col, char[][] buffer)
    {
        StringBuilder sb = new StringBuilder(9);
        for(int i = -1; i <= 1; i++)
        {
            for (int j = -1; j <= 1; j++)
            {
                if(SafeGet(row + i, col + j, buffer) == '#')
                {
                    sb.append('#');
                } else
                {
                    sb.append(' ');
                }
            }
        }

        return sb.toString();
    }

    private char SafeGet(int row, int col, char[][] buffer)
    {
        if (row < 0 || col < 0 || row >= buffer.length || col >= buffer[row].length)
            return ' ';
        else
            return buffer[row][col];
    }
}
