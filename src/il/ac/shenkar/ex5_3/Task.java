package il.ac.shenkar.ex5_3;

public class Task 
{
    private String description;
    private int id;

    public Task(int id, String description) 
    {
        this.description = description;
        this.id = id;
    }

    public Task() {}
    
    public int getId()
    {
        return id;
    }
    
    public void setId(int id)
    {
    	this.id=id;
    }
    
    public String getDescription() 
    {
        return description;
    }

    public void setDescription(String description)
    {
    	this.description=description;
    }
    
    public String toString()
    {
        return getDescription();
    }  
}