import java.io.*;

public class Minesweeper
{
    public static void main(String[] args)
    {
        String poanglistaFil = "Highscore.txt";
        int val = meny();
        if (val == 2)
        {
            poanglista(poanglistaFil);
        }
        int planareaMinimum = 450;
        int minantalMinimum = (int) (planareaMinimum / 8);
        System.out.print("Minfaltets langd:");
        int planLangd = Keyboard.readInt();
        System.out.print("Minfaltets bredd:");
        int planBredd = Keyboard.readInt();
        if (planBredd > 75 ||
            planLangd > 30 ||
            planBredd < 1 ||
            planLangd < 1)
        {
            System.out.println("Alltfor stor plan!");
            System.exit(0);
        }
        char[][] plan = new char[planLangd + 2][planBredd + 2];
        for (int i = 0;i < plan.length;i++)
        {
            plan[i][0] = '>';
            plan[i][plan[0].length - 1] = '<';
        }
        for (int i = 0;i < plan[0].length;i++)
        {
            plan[0][i] = 'V';
            plan[plan.length - 1][i] = '^';
        }
        plan[0][0] = '#';
        plan[0][plan[0].length - 1] = '#';
        plan[plan.length - 1][0] = '#';
        plan[plan.length - 1][plan[0].length - 1] = '#';
        boolean[][] visare = new boolean[plan.length][plan[0].length];
        for (int i = 0;i < plan.length;i++)
        {
            for (int j = 0;j < plan[0].length;j++)
            {
                if (plan[i][j] == 'V' ||
                    plan[i][j] == '<' ||
                    plan[i][j] == '^' ||
                    plan[i][j] == '>' ||
                    plan[i][j] == '#')
                {
                    visare[i][j] = true;
                }
                else
                {
                    visare[i][j] = false;
                }
            }
        }
        int x = 0;
        int y = 0;
        int[] xSteg = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] ySteg = {-1, 0, 1, -1, 1, -1, 0, 1};
        int minax = 0;
        int minay = 0;
        int g = 0;
        System.out.print("Antal minor:");
        int antalMinor = Keyboard.readInt();
        if (antalMinor >= ((plan.length - 2) * (plan[0].length - 2)) ||
            antalMinor < 1)
        {
            System.out.println("Fel antal minor!");
            System.exit(0);
        }
        for (int i = 0;i < antalMinor;i++)
        {
            minax = (int) (Math.random() * (plan[0].length - 2) + 1);
            minay = (int) (Math.random() * (plan.length - 2) + 1);
            if (plan[minay][minax] == 'X')
            {
                i--;
            }
            plan[minay][minax] = 'X';
        }
        char siffra = 'd';
        int antalNarliggande = 0;
        for (int i = 1;i <= (plan.length - 2);i++)
        {
            for (int j = 1;j <= (plan[0].length - 2);j++)
            {
                for (int k = 0;k <= 7;k++)
                {
                    if (plan[i + ySteg[k]][j + xSteg[k]] == 'X')
                    {
                        antalNarliggande++;
                    }
                }
                if (plan[i][j] != 'X')
                {
                    siffra = (char) (48 + antalNarliggande);
                    plan[i][j] = siffra;
                }
                antalNarliggande = 0;
            }
        }
        ThreadEx tx = new ThreadEx();
        tx.start();
        while (true)
        {
            for (int i = 0;i < plan.length;i++)
            {
                for (int j = 0;j < plan[0].length;j++)
                {
                    if (visare[i][j] == true)
                    {
                        System.out.print(plan[i][j]);
                    }
                    else if (visare[i][j] == false)
                    {
                        System.out.print(".");
                    }
                }
                System.out.println("");
            }
            System.out.print("x:");
            x = Keyboard.readInt();
            System.out.print("y:");
            y = Keyboard.readInt();
	        int tid = tx.getTime();
            System.out.println("Tid: "+ tid +" sekunder");
            while (x < 1 ||
                   x > (plan[0].length - 2) ||
                   y < 1 ||
                   y > (plan.length - 2))
            {
                System.out.println("Koordinaterna finns ej!");
                System.out.print("x:");
                x = Keyboard.readInt();
                System.out.print("y:");
                y = Keyboard.readInt();
            }
            visare[y][x] = true;
            if (plan[y][x] == '0')
            {
                for (int i = 0;i <= 7;i++)
                {
                    visare[y + ySteg[i]][x + xSteg[i]] = true;
                }
            }
            if (plan[y][x] == 'X')
            {
                System.out.println("Du trampade pa en mina! Spelet ar slut.");
                System.exit(0);
            }
            for (int i = 1;i <= (plan.length - 2);i++)
            {
                for (int j = 1;j <= (plan[0].length - 2);j++)
                {
                    if (visare[i][j] == true ||
                        plan[i][j] == 'X')
                    {
                        g++;
                    }
                }
            }
            if (g == (plan.length - 2) * (plan[0].length - 2))
            {
                int speltid = tx.getTime();
                System.out.println("Grattis, du har sakrat omradet!");
                if ((planLangd * planBredd) >= planareaMinimum &&
                    antalMinor >= minantalMinimum)
                {
                    System.out.print("Vad ar ditt namn?:");
                    String poangNamn = Keyboard.readString();
                    try
                    {
                        boolean laggTill = true;
                        FileWriter fw = new FileWriter(poanglistaFil, laggTill);
                        BufferedWriter bw = new BufferedWriter(fw);
                        PrintWriter filskrivare = new PrintWriter(bw);
                        filskrivare.print(poangNamn + ": ");
                        filskrivare.println(speltid + " sekunder");
                        filskrivare.close();
                    }
                    catch (IOException e)
                    {
                        System.out.println("Skrivningen misslyckades.");
                    }
                }
                System.exit(0);
            }
            g = 0;
        }
    }
    static int meny()
    {
        System.out.println("Valkommen att spela Minesweeper! Vill du:\n1.Spela\n2.Se poanglista");
        int menyVal = Keyboard.readInt();
        return menyVal;
    }
    static void poanglista(String filnamn)
    {
        try
        {
            FileReader fr = new FileReader(filnamn);
            BufferedReader fillasare = new BufferedReader(fr);
            String rad = fillasare.readLine();
            while (rad != null)
            {
                System.out.println(rad);
                rad = fillasare.readLine();
            }
        }
        catch (FileNotFoundException e1)
        {
            System.out.println("Filen hittades inte.");
        }
        catch (IOException e2)
        {
            System.out.println(e2);
        }
    }
}

class ThreadEx extends Thread
{
    int tid;
    public void run()
    {
        tid = 0;
        try
        {
            while(true)
            {
                Thread.sleep(1000);
                tid++;
            }
        }
        catch(InterruptedException ex)
        {
            ex.printStackTrace();
        }
    }
    int getTime()
    {
	return tid;
    }

}