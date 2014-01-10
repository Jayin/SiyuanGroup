package com.alumnigroup.entity;

/**
 * 微博
 * @author vector
 *
 */
public class Tweet {
	int type = 2;
	
	
	public int getType() {
		return type;
	}
	
	public int getType1() {
		int i = 3;
		
		double random = Math.random();
		
		if(random < 1.0/i){
			return 0;
		}else if(random < 2.0/i){
			return 1;
		}else{
			return 2;
		}
	}

}
