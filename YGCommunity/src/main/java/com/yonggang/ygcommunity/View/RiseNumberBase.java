package com.yonggang.ygcommunity.View;

public interface RiseNumberBase 
{
    public void start();
    
    public RiseNumberTextView withNumber(float number);
 
    public RiseNumberTextView withNumber(float number, boolean flag);
 
    public RiseNumberTextView withNumber(int number);
 
    public RiseNumberTextView setDuration(long duration);
 
    public void setOnEnd(RiseNumberTextView.EndListener callback);
}
