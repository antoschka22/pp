/**
 * Implementierung des von MSet.before() zurückgegebenen Containers.
 * Diese Klasse ist package-private.
 * Sie implementiert die Logik für den "Zwischen"-Container.
 */
class MSetResultImpl<E extends Modifiable<X, E>, X> implements MSetResult<E> {


    @Override
    public MSetResult<E> add(E e) {
        return null;
    }

    @Override
    public MSetResult<E> subtract(E e) {
        return null;
    }

    @Override
    public Boolean before(E x, E y) {
        return null;
    }

    @Override
    public void setBefore(E x, E y) {

    }
}