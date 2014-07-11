package misc;

public class Tuple<X, Y>
{

  public final X x;
  public final Y y;

  public Tuple(X x, Y y)
  {
    this.x = x;
    this.y = y;
  }

  public Tuple(X x)
  {
    this.x = x;
    this.y = null;
  }

  @Override
  public String toString()
  {
    return x.toString();
  }

}
