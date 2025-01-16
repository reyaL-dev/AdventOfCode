import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Day17PartTwo implements AoCMain.AoCHandler
{

    static final int ADV = 0, BXL = 1, BST = 2, JNZ = 3, BXC = 4, OUT = 5, BDV = 6, CDV = 7;

    private RegisterProgram parseInstructions(RegisterProgram input, long iteration) throws Exception
    {

        input.setRegisterA(iteration);

        int i = 0;
        while (i < input.program.size())
        {
            int jump = input.execInstruction(input.program.get(i), input.program.get(i + 1));
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

        long registerA = 0;
        long registerB = 0;
        long registerC = 0;
        List<Integer> program = new ArrayList<>();

        for (String line : lines)
        {
            line = line.trim();
            if (line.startsWith("Register A:"))
            {
                registerA = Long.parseLong(line.split(": ")[1]);
            } else if (line.startsWith("Register B:"))
            {
                registerB = Long.parseLong(line.split(": ")[1]);
            } else if (line.startsWith("Register C:"))
            {
                registerC = Long.parseLong(line.split(": ")[1]);
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
        RegisterProgram inputDebug = read(inputPath);
        String listString = inputDebug.program.stream().map(Object::toString).collect(Collectors.joining(","));
        long i = 107413700225432L;
        while (true)
        {
            RegisterProgram input = inputDebug.clone();
            parseInstructions(input, i);
            if (input.getOutput().equals(listString))
            {
                break;
            }
            String result = input.getOutput();
            String octal = Long.toOctalString(i);

            // MANUAL APPROACH!!!
            // If pattern is correct, multiply by 8, then increment by 1 until pattern match again.
            i *= 8;
            i++;
        }
        return Long.toString(i);
    }

    class RegisterProgram implements Cloneable
    {

        private long registerA;
        private long registerB;
        private long registerC;
        private List<Integer> program;
        private String output = "";

        public RegisterProgram(long registerA, long registerB, long registerC, List<Integer> program)
        {
            this.registerA = registerA;
            this.registerB = registerB;
            this.registerC = registerC;
            this.program = new ArrayList<Integer>(program);
        }

        public String getOutput()
        {
            return output.substring(0, output.length() - 1);
        }

        private void addOutput(String input)
        {
            this.output += input + ",";
        }

        public long getRegisterA()
        {
            return registerA;
        }

        public void setRegisterA(long registerA)
        {
            this.registerA = registerA;
        }

        public long getRegisterB()
        {
            return registerB;
        }

        public void setRegisterB(long registerB)
        {
            this.registerB = registerB;
        }

        public long getRegisterC()
        {
            return registerC;
        }

        public void setRegisterC(long registerC)
        {
            this.registerC = registerC;
        }

        public List<Integer> getProgram()
        {
            return new ArrayList<>(program);
        }

        public void setProgram(List<Integer> program)
        {
            this.program = new ArrayList<Integer>(program);
        }

        public int execInstruction(Integer opCode, int operand)
        {
            long opValue = 0;
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
                    setRegisterA((long) (getRegisterA() / (Math.pow(2, opValue))));
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
                    addOutput(Long.toString(opValue % 8));
                    break;
                case BDV:
                    setRegisterB((long) (getRegisterA() / (Math.pow(2, opValue))));
                    break;
                case CDV:
                    setRegisterC((long) (getRegisterA() / (Math.pow(2, opValue))));
                    break;
                default:
                    System.out.println("Invalid opCode!");
                    break;
            }
            return -1;
        }

        @Override
        public RegisterProgram clone()
        {
            try
            {
                RegisterProgram clone = (RegisterProgram) super.clone();
                // TODO: copy mutable state here, so the clone can't change the internals of the original
                return clone;
            }
            catch (CloneNotSupportedException e)
            {
                throw new AssertionError();
            }
        }
    }
}

