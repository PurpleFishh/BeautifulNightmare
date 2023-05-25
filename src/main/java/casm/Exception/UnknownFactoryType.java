package casm.Exception;

/**
 * Exception thrown when the type given to the factory was not found
 * @see casm.Factory.FactoryTypes
 * @see casm.Factory.EntityFactory.EntityFactory
 */
public class UnknownFactoryType extends Exception{

    public UnknownFactoryType()
    {
        super("The type given to the factory was not found!");
    }
}
