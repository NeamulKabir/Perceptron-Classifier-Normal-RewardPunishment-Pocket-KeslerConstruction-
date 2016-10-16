/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package perceptron;

/**
 *
 * @author Neamul Kabir
 */
public class RewardPunishment {
    double[][] features;
    int[] trainClass;
    int totalClasses,featureCount,limit;
    double[] weights;
    double learningRate = 0.2;
    
    RewardPunishment(double[][] f, int[] t, int tc, int fc, int l)
    {
        features = f;
        trainClass = t;
        totalClasses = tc;
        featureCount = fc;
        limit = l;
        weights = new double[featureCount+1];
        initWeight();
    }
    
    private void initWeight()
    {
        double sum=0,sum2=0;
        for(int i=0;i<=featureCount;i++)
        {weights[i] = Math.random();sum+=weights[i];}
        for(int i=0;i<=featureCount;i++)
        {
            weights[i] = weights[i]/sum;
            sum2 += weights[i];
            //weights[i] = 0.0;
            //System.out.println(weights[i]);
        }
        //System.out.println(sum2);
    }
    
    
    double[] solve()
    {
        for(int i=0;i<3;i++)
        {
            for(int j=0;j<limit;j++)
            {
                int output =calculateOutput(j, weights) ;
                if(trainClass[j] != output)
                {
                    double delta;
                    if(trainClass[j] == 1)
                        delta = -1;
                    else    delta = 1;
                    for(int p=0;p<featureCount;p++)
                    {
                        weights[p] -=  learningRate * delta * features[p][j];
                    }
                    weights[featureCount] -= learningRate * delta * 1;
                }
            }
            double acc = checkTrainingAccuracy();
            if(acc==100.0)
                break;
        }
        return weights;
    }
    
    private int calculateOutput(int index, double w[])
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
    
    double checkTrainingAccuracy()
    {
        int succ=0,fail=0;
        for(int i=0;i<limit;i++)
        {
            int out = calculateOutput(i, weights);
            if(trainClass[i] == out)
                succ++;
            else    fail++;
        }
        System.out.println("Succ : "+succ+"\tFail : "+fail);
        return 100.0*succ/limit;
    }
}
