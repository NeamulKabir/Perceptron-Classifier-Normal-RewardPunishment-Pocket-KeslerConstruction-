/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package perceptron;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Neamul Kabir
 */
public class Perceptron {

    /**
     * @param args the command line arguments
     */
    static int totalClasses,featureCount,limit,testLimit;
    static double[][] features, testFeature;
    static double[] outWeights;
    static int[] trainClass,testClass;
    public static void main(String[] args) throws Exception {
        // TODO code application logic here
        
        readTraining("train1");
        readTestSet("test1");
        NormalAlgo na = new NormalAlgo(features, trainClass, totalClasses, featureCount, limit);
        outWeights = na.solve();
        System.out.println("Normal Algorithm ACCuracy : "+testData()+"\n\n");
        
        RewardPunishment nr = new RewardPunishment(features, trainClass, totalClasses, featureCount, limit);
        outWeights = nr.solve();
        System.out.println("Reward Punishment Algorithm ACCuracy : "+testData()+"\n\n");
        
        PocketAlgo n = new PocketAlgo(features, trainClass, totalClasses, featureCount, limit);
        outWeights = nr.solve();
        System.out.println("Pocket Algorithm ACCuracy : "+testData()+"\n\n");
        
        readTraining("train2");
        readTestSet("test2");
        KeslerConstruction nk = new KeslerConstruction(features, trainClass, totalClasses, featureCount, limit);
        double succ=0,fail=0;
        for(int i=0;i<testLimit;i++)
        {
            double[] sample = new double[featureCount+1];
            for(int j=0;j<featureCount;j++)
                sample[j] = testFeature[j][i];
            sample[featureCount] = 1;
            int out = nk.testSample(sample);
            if(out == testClass[i])
                succ++;
            else
                fail++;
        }
        System.out.println("Success: "+succ+"\t\tFail: "+fail);
        System.out.println("Kesler's Construction Accuracy : "+(100.0*succ/testLimit));
        //n.transpose();
        //n.solve();
        //outWeights = n.weights;
        //outWeights = n.bestWeight;
        //System.out.println("ACCuracy : "+testData());
        //printTrainingSet();
        //printTestSet();
        
    }
    
    static double testData()
    {
        double succ=0,fail=0;
        for(int i=0;i<testLimit;i++)
        {
            int out = calculateOutput(i, outWeights);
            if(testClass[i] == out)
                succ++;
            else    fail++;
        }
        System.out.println("\n\nSucc : "+succ+"\tFail : "+fail);
        return (100.0*succ/(succ+fail));
    }
    
    static int calculateOutput(int index, double w[])
    {
        double result=0;
        for(int i=0;i<featureCount;i++)
        {
            result += w[i]*features[i][index]; 
        }
        result += w[featureCount];
        if(result > 0)
            return 1;
        else
            return 2;
    }
    
    static void readTraining (String file)
    {
        try{
            BufferedReader brTopics = new BufferedReader(new FileReader(file+".txt"));
            String line = brTopics.readLine();
            String str[] = line.split(" ");
            featureCount = Integer.valueOf(str[0]);
            totalClasses = Integer.valueOf(str[1]);
            limit = Integer.valueOf(str[2]);
            features = new double[featureCount][limit];
            trainClass = new int[limit];
            outWeights = new double[featureCount+1];
            int count=0;
            while((line = brTopics.readLine()) !=null)
            {
                //System.out.println(line);
                line = line.replaceAll("\\s+", " ");
                //line.trim();
                //System.out.println(line);
                String s[] = line.split(" ");
                for(int i=1;i<s.length-1;i++)
                {
                    //System.out.println(s[i] + " "+i);
                    features[i-1][count] = Double.valueOf(s[i]);
                }
                trainClass[count] = Integer.valueOf(s[s.length-1]);
                count++;
            }
        } catch (Exception ex) {
            Logger.getLogger(Perceptron.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    static void readTestSet(String file)
    {
        try{
            BufferedReader brTopics = new BufferedReader(new FileReader(file+".txt"));
            String line;
            testFeature = new double[featureCount][limit];
            testClass = new int[limit];
            int count=0;
            while((line = brTopics.readLine()) !=null)
            {
                //System.out.println(line);
                line = line.replaceAll("\\s+", " ");
                //line.trim();
                //System.out.println(line);
                String s[] = line.split(" ");
                for(int i=1;i<s.length-1;i++)
                {
                    //System.out.println(s[i] + " "+i);
                    testFeature[i-1][count] = Double.valueOf(s[i]);
                }
                testClass[count] = Integer.valueOf(s[s.length-1]);
                count++;
            }
            testLimit = count;
            //System.out.println(testLimit);
        } catch (Exception ex) {
            Logger.getLogger(Perceptron.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    static void printTrainingSet()
    {
        for(int i=0;i<limit;i++)
        {
            for(int j=0;j<featureCount;j++)
            {
                System.out.print(features[j][i]+"\t");
            }
            System.out.println(trainClass[i]);
        }
    }
    
    static void printTestSet()
    {
        for(int i=0;i<testLimit;i++)
        {
            for(int j=0;j<featureCount;j++)
            {
                System.out.print(testFeature[j][i]+"\t");
            }
            System.out.println(testClass[i]);
        }
    }
    
}
