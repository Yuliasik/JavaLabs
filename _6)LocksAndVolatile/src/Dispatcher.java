import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Dispatcher {
	static double sum = 0;
	public static void main(String[] args) throws InterruptedException {
		String path = "src\\";
		Thread t1 = new Thread(new FileHandler(path + "file1.txt"));
		Thread t2 = new Thread(new FileHandler(path + "file2.txt"));
		Thread t3 = new Thread(new FileHandler(path + "file3.txt"));
		t1.start(); t2.start(); t3.start();
		t1.join(); t2.join(); t3.join();
		System.out.println("Загальна сума = " + sum);
	}
}

class FileHandler implements Runnable {
	Pattern pattern = Pattern.compile("-?\\d+\\.?\\d*");
	String fileName;
	static Lock lock = new ReentrantLock();

	FileHandler(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public void run() {
		this.readFile();
	}

	void readFile() {
		try {
			BufferedReader buffer = new BufferedReader(new FileReader(new File(this.fileName)));
			String line = buffer.readLine();
			while (line != null) {
				findCountfWords(line);
				line = buffer.readLine();
			}
			buffer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	void findCountfWords(String line) {
		Matcher matcher = pattern.matcher(line);
		while (matcher.find()) {
			lock.lock();
			try {
				Dispatcher.sum += Double.parseDouble(matcher.group());
			} finally {
				lock.unlock();
			}
		}
	}
}


/*
 	float f1 = 0; 00000000...(32) or (64)
 	
  0.0000000...01    H = 0 1.00000000 00000000 00000000
  
  Нескінченності
   +    0ххххх...   H = 01111111 m = 00000000000000000000000   POSITIVE.INFINITY    \
   -    1ххххх...   H = 11111111 m = 00000000000000000000000   NEGATIVE.INFINITY    /  for float
  
  NaN - not a number
                    H = 11111111 m = 00000000000000100000001   NaN
                                    (хоча б 1 ненульовий біт)
  
  
   float  - 4 bytes
  double - 8 bytes
  
  short format     - 3 bytes = 24 bits:
         numberSign(1)  Characteristic(8)   mantice(15-1)             42 2C B8
             0             10000100     010110010111000 =>  1.0100 0010 0010 1100 1011 1000 
  
  float x = 43.18f:
          numberSign(1)  Characteristic(8)   mantice(24-1)                 42 2C B8 51
               0             10000100     01011001011100001010001 =>  1.0100 0010 0010 1100 1011 1000 0101 0001 
  
  double x = 43.18:
          numberSign(1)  Characteristic(8)       mantice(52)    
               0            10000000100    01011001011100001010001...
 
  
  ----------------------------------------------------------------
 3.18   = 0.318 * 10**1  = 31.8 * 10**-1
 0.0974 = 0.974 * 10**-1 = 9.74 * 10 **-2
 
 0.318 * 10**1  - "нормальне" класичне представлення 
 0.974 * 10**-1
 
 "нормальне" представлення за IEEE-754  
 3.18 * 10**0  мантиса(3.18) і порядок(0)
 9.74 * 10**-2
 
 0.1000000 * 10 ** -max 0.000000...01 machine zero
 1.0000000 * 10 ** -max 0.000000...01
 
 (x-y) * 1000000000000000000000 => not zero!       
           
float - 4 bytes  
double - 8 bytes          
numberSign1              мантиса15-1        222СB8
       0   10000100   010110010111000 => 01 000010 00101100 10111000 01010001 
        характеристика8  
 43.18 = 101011.0010111000010 = 1.010110010111000 01010001 * 2**5             H=P+127;
 0.18                           мантиса                   порядок       H=5+127 = 132 = 10000010 
 -----                                                            Hdouble=5+1023= 1028 10000000100
 0.36
 0.72
 1.44 - 1
 0.88
 1.76 - 1
 1.52 - 1
 1.04 - 1
 0.08 - 1
 0.16
 0.32
 0.64
 1.28 - 1
 0.56
 1.12 - 1
 0.24
 0.48
 0.96
 1.92 - 1
 ----
 1.84
 1.68
 1.36
 0.72
 

 
 
 
 
 
 */



