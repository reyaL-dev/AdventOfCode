import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Day17PartOne implements AoCMain.AoCHandler
{

    static final int ADV = 0, BXL = 1, BST = 2, JNZ = 3, BXC = 4, OUT = 5, BDV = 6, CDV = 7;

    private RegisterProgram parseInstructions(String inputPath) throws Exception
    {
        RegisterProgram input = read(inputPath);

        int i = 0;
        while (i < input.program.size())
        {
            int jump = input.execInstruction(input.program.get(i), input.program.get(i+1));
            if (jump == -1)
            {
                i += 2;
            } else
            {
                i = jump;
            }
        }
        return input;
    }

    @Override
    public RegisterProgram read(String path) throws Exception
    {
        List<String> lines = Files.readAllLines(Path.of(path));

        int registerA = 0;
        int registerB = 0;
        int registerC = 0;
        List<Integer> program = new ArrayList<>();

        for (String line : lines)
        {
            line = line.trim();
            if (line.startsWith("Register A:"))
            {
                registerA = Integer.parseInt(line.split(": ")[1]);
            } else if (line.startsWith("Register B:"))
            {
                registerB = Integer.parseInt(line.split(": ")[1]);
            } else if (line.startsWith("Register C:"))
            {
                registerC = Integer.parseInt(line.split(": ")[1]);
            } else if (line.startsWith("Program:"))
            {
                String[] numbers = line.split(": ")[1].split(",");
                for (String number : numbers)
                {
                    program.add(Integer.parseInt(number.trim()));
                }
            }
        }

        return new RegisterProgram(registerA, registerB, registerC, program);
    }

    @Override
    public String getSolutionString(String inputPath) throws Exception
    {
        RegisterProgram inputDebug = parseInstructions(inputPath);
        return inputDebug.getOutput();
    }

    class RegisterProgram
    {

        private int registerA;
        private int registerB;
        private int registerC;
        private List<Integer> program;
        private String output = "";

        public RegisterProgram(int registerA, int registerB, int registerC, List<Integer> program)
        {
            this.registerA = registerA;
            this.registerB = registerB;
            this.registerC = registerC;
            this.program = new ArrayList<>(program);
        }

        public String getOutput()
        {
            return output.substring(0, output.length() - 1 );
        }

        private void addOutput(String input)
        {
            this.output += input + ",";
        }

        public int getRegisterA()
        {
            return registerA;
        }

        public void setRegisterA(int registerA)
        {
            this.registerA = registerA;
        }

        public int getRegisterB()
        {
            return registerB;
        }

        public void setRegisterB(int registerB)
        {
            this.registerB = registerB;
        }

        public int getRegisterC()
        {
            return registerC;
        }

        public void setRegisterC(int registerC)
        {
            this.registerC = registerC;
        }

        public List<Integer> getProgram()
        {
            return new ArrayList<>(program);
        }

        public void setProgram(List<Integer> program)
        {
            this.program = new ArrayList<>(program);
        }

        public int execInstruction(int opCode, int operand)
        {
            int opValue = 0;
            switch (operand)
            {
                case 0, 1, 2, 3 -> opValue = operand;
                case 4 -> opValue = this.registerA;
                case 5 -> opValue = this.registerB;
                case 6 -> opValue = this.registerC;
                default -> System.out.println("Invalid Operand!");
            }

            switch (opCode)
            {
                case ADV:
                    setRegisterA((int) (getRegisterA() / (Math.pow(2, opValue))));
                    break;
                case BXL:
                    setRegisterB(getRegisterB() ^ operand);
                    break;
                case BST:
                    setRegisterB(opValue % 8);
                    break;
                case JNZ:
                    if (getRegisterA() != 0)
                    {
                        return operand;
                    }
                    break;
                case BXC:
                    setRegisterB(getRegisterB() ^ getRegisterC());
                    break;
                case OUT:
                    addOutput(Integer.toString(opValue % 8));
                    break;
                case BDV:
                    setRegisterB((int) (getRegisterA() / (Math.pow(2, opValue))));
                    break;
                case CDV:
                    setRegisterC((int) (getRegisterA() / (Math.pow(2, opValue))));
                    break;
                default:
                    System.out.println("Invalid opCode!");
                    break;
            }
            return -1;
        }
    }
}

