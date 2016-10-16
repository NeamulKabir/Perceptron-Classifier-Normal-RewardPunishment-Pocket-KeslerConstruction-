/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package perceptron;

import java.util.ArrayList;

/**
 *
 * @author Neamul Kabir
 */
public class KeslerConstruction {
    double[][] features,kFeatures;
    int[] trainClass;
    int totalClasses,featureCount,limit;
    double[][] weights,bestWeight;
    double learningRate = 0.2;
    ArrayList<double[][]> KSample;
    double[][][] ksamples;
    int id=0,keslerCount;
    
    KeslerConstruction(double[][] f, int[] t, int tc, int fc, int l)
    {
        features = f;
        trainClass = t;
        totalClasses = tc;
        featureCount = fc;
        limit = l;
        keslerCount = limit * (totalClasses-1);
        weights = new double[featureCount+1][totalClasses];
        kFeatures = new double[featureCount+1][totalClasses];
        KSample = new ArrayList<>();
        ksamples = new double[keslerCount][featureCount+1][totalClasses];
        initWeight();
        trainSample();
        solve();
    }
    
    private void initWeight()
    {
        for(int i=0;i<=featureCount;i++)
        {
            for(int j=0;j<totalClasses;j++)
                weights[i][j] = Math.random();
        }
        
    }
    void trainSample()
    {
        for(int i=0;i<limit;i++)
        {
            constructSample(i,trainClass[i]-1);
        }
        System.out.println(KSample.size());
        double[][] test = KSample.get(1);
        /*for(int i=0;i<=featureCount;i++)
        {
            for(int j=0;j<totalClasses;j++)
            {
                System.out.print(ksamples[599][i][j]+" ");
            }
            System.out.println();
        }*/
    }
    void constructSample(int index, int classId)
    {
        double[][] tempSample = new double[featureCount+1][totalClasses];
        for(int i=0;i<featureCount;i++)
        {
            tempSample[i][classId] = features[i][index];
            
        }
        tempSample[featureCount][classId] = 1;
        
        int count=0;
        for(int i=0;i<totalClasses;i++)
        {
            if(i!=classId)
            {
                for(int j=0;j<featureCount;j++)
                {
                    tempSample[j][i] = -features[j][index];
                    
                }
                tempSample[featureCount][i] = -1;
                
                KSample.add(tempSample);
                //ksamples[id++] = tempSample;
                for(int p=0;p<=featureCount;p++)
                {
                    for(int q=0;q<totalClasses;q++)
                    {
                        ksamples[id][p][q] = tempSample[p][q];
                    }
                }
                id++;
                for(int j=0;j<featureCount;j++)
                {
                    tempSample[j][i] = 0;
                }
                tempSample[featureCount][i] = 0;
               /* System.out.println("$$\n");
                double[][] test = KSample.get(count);
                for(int p=0;p<=featureCount;p++)
                {
                    for(int q=0;q<totalClasses;q++)
                    {
                        System.out.print(ksamples[id-1][p][q]+" ");
                    }
                    System.out.println();
                }
                System.out.println("##\n");*/
                count++;
            }
        }
    }
    void solve()
    {
        for(int i=0;i<100;i++)
        {
            double[][] temp = new double[featureCount+1][totalClasses];
            for(int j=0;j<keslerCount;j++)
            {
                double[][] sample = ksamples[j];
                double result = dotProduct(transpose(sample), transpose(weights));
                if(result<=0)
                {
                    for(int k=0;k<=featureCount;k++)
                    {
                        for(int l=0;l<totalClasses;l++)
                            temp[k][l] += sample[k][l];
                    }
                }
            }
            for(int k=0;k<=featureCount;k++)
            {
                for(int l=0;l<totalClasses;l++)
                    weights[k][l] += learningRate*temp[k][l];
            }
            double accu = checkTrain();
            if(accu == 100.0)
                break;
        }
    }
    
    double checkTrain()
    {
        double succ=0,fail=0;
        for(int j=0;j<keslerCount;j++)
        {
            double[][] sample = ksamples[j];
            double result = dotProduct(transpose(sample), transpose(weights));
            if(result>0)
                succ++;
            else
                fail++;
        }
        System.out.println("Succ: "+succ+"\tFail: "+fail);
        return 100.0*succ/(double)keslerCount;
    }
    
    double dotProduct(double[][] w, double[][] x)
    {
        double sum=0;
        for(int i=0;i<totalClasses;i++)
        {
            for(int j=0;j<=featureCount;j++)
            {
                sum += w[i][j]*x[i][j];
            }
        }
        return sum;
    }
    
    double[][] transpose(double[][] w)
    {
        double[][] tempWeight = new double[totalClasses][featureCount+1];
        for(int j=0;j<totalClasses;j++)
        {
            for(int i=0;i<=featureCount;i++)
            {
                tempWeight[j][i] = w[i][j];
                //System.out.print(tempWeight[j][i]+" ");
            }
            //System.out.println();
        }
        return tempWeight;
    }
    
    int testSample(double[] x)
    {
        double[][] temp = transpose(weights);
        int output = matrixMultiply(temp, x);
        return output;
    }
    
    int matrixMultiply(double[][] w, double[] x)
    {
        double result,max=-9999;
        int index=-1;
        for(int i=0;i<totalClasses;i++)
        {
            result=0;
            for(int j=0;j<=featureCount;j++)
            {
                result += w[i][j]*x[j];
            }
            if(result>max)
            {
                max = result;
                index = i;
            }
        }
        return index+1;
    }
    
}
