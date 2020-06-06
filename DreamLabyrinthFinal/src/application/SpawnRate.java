package application;

import java.util.Arrays;
/**
 * SpawnRate releases a set spawnrate (based on enemytype and prior calculations).
 * The methods are used for Labyrinth mode to help adjust the spawn rates as the player progresses to
 * eventually get to the point that the game tries it's hardest to kill the player.
 * @author Quinn
 *
 */
public class SpawnRate implements Universals {
	double[] SpawnStats;
	
	public SpawnRate() //Default spawn rate for all enemies is generated here and rounded to 5 decimal places.
	{
		SpawnStats = new double[12];
		double value = 100;
		for (int i = 0; i < 12; i++)
		{
			if (i == 0)
			{
				SpawnStats[i] = 0.6*value;
			}
			else if (i ==11)
			{
				SpawnStats[i]= (double)Math.round(value*0.6*100000d)/100000d;
			}
			else
			{
				value = value - SpawnStats[i-1];
				SpawnStats[i] = (double)Math.round(value*0.6*100000d)/100000d;
			}
		}
	}
	
	public double Total() //Balances out the values to make sure it is equal to 100
	{
		double value =0;
		double variation = 0;
		for (int i = 0; i < SpawnStats.length; i++)
		{
			value = value + SpawnStats[i];
		}
		if (value != 100)
		{
			variation = (100 - value)/12;
			for (double rate : SpawnStats)
			{
				rate = (double)Math.round((rate+variation) * 100000d)/100000d;
			}	
		}
		return value;
	}
	
	public void Update(int i) //Updates the spawn rate of the specified index.
	{
		double value = SpawnStats[i];
		value = 0.9*value;
		SpawnStats[i] = (double)Math.round(value * 100000d)/100000d;
	}
	
	public void Balance(int i) //Balances the values of the spawn rate to ensure the all rates are changed when 1 rate is.
	{
		double value = 100;
		for (int j = 0; j < 12; j++)
		{
			if (j<=i)
			{
				SpawnStats[j] = SpawnStats[j];
				value = value - SpawnStats[j];
			}
			else if (j ==11)
			{
				SpawnStats[j]= (double)Math.round(value*100000d)/100000d;
			}
			else
			{
				SpawnStats[j] = (double)Math.round(value*0.6*100000d)/100000d;
				value = value - SpawnStats[j];
			}
		}
		Total(); //Balances out the values to make sure it is equal to 100
	}
	
	public double get(int position)
	{
		return SpawnStats[position];
	}
	
	public void PhaseOne() // Significantly reduces the spawn rate for the first three enemy types.
	{
		SpawnStats[0] = 10;
		SpawnStats[1] = 10;
		SpawnStats[2] = 10;
		Balance(3);
	}
	
	//prevents the spawning of the first three enemy types and reduces the next three
	public void PhaseTwo()
	{
		SpawnStats[0] = 0;
		SpawnStats[1] = 0;
		SpawnStats[2] = 0;
		SpawnStats[3] = 5;
		SpawnStats[4] = 10;
		SpawnStats[5] = 10;
		Balance(5);
	}
	//sets the chance of spawning the first 6 enemy types to 0
	public void PhaseThree()
	{
		SpawnStats[0] = 0;
		SpawnStats[1] = 0;
		SpawnStats[2] = 0;
		SpawnStats[3] = 0;
		SpawnStats[4] = 0;
		SpawnStats[5] = 0;
		Balance(5);
	}
	
	//sets the first 8 enemy types to not spawn
	public void PhaseFour() {
		for (int i = 0; i < 8; i++)
		{
			SpawnStats[i] = 0;
		}
		Balance(8);
	}
	
	//only the bosses spawn.
	public void PhaseFive()
	{
		for (double stat : SpawnStats)
		{
			stat = 0;
		}
		SpawnStats[10] = 50;
		SpawnStats[10] = 50;
	}

}
