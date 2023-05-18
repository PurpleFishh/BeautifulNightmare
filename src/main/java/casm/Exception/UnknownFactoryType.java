package casm.Exception;

public class UnknownFactoryType extends Exception{

    public UnknownFactoryType()
    {
        super("The type given to the factory was not found!");
    }
}
