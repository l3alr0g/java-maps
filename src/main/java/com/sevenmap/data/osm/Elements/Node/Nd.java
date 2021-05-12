package com.sevenmap.data.osm.Elements.Node;

import java.util.Objects;

import com.sevenmap.data.osm.parser.Annotations.XMLAttribute;

public class Nd {

  @XMLAttribute(unique = true)
  private Integer ref;

  public Nd() {
  }

  public Nd(Integer ref) {
    this.ref = ref;
  }

  public Integer getRef() {
    return this.ref;
  }

  public void setRef(Integer ref) {
    this.ref = ref;
  }

  public Nd ref(Integer ref) {
    setRef(ref);
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (o == this)
      return true;
    if (!(o instanceof Nd)) {
      return false;
    }
    Nd nd = (Nd) o;
    return Objects.equals(ref, nd.ref);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(ref);
  }

  @Override
  public String toString() {
    return "{" + " ref='" + getRef() + "'" + "}";
  }

}
