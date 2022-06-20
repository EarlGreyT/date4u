package de.earlgreyt.date4u.core.formdata;

import de.earlgreyt.date4u.core.formdata.ProfileFormData;
import java.util.ArrayList;
import java.util.List;

public class SearchData {

  private int minSize;
  private int maxSize;
  private int minAge;
  private int maxAge;
  private boolean considerMinSize;
  private boolean considerMaxSize;
  private boolean considerMinAge;
  private boolean considerMaxAge;

  public SearchData() {
  }

  public SearchData(int minSize, int maxSize, int minAge, int maxAge, boolean considerMinSize,
      boolean considerMaxSize, boolean considerMinAge, boolean considerMaxAge) {
    this();
    this.minSize = minSize;
    this.maxSize = maxSize;
    this.minAge = minAge;
    this.maxAge = maxAge;
    this.considerMinSize = considerMinSize;
    this.considerMaxSize = considerMaxSize;
    this.considerMinAge = considerMinAge;
    this.considerMaxAge = considerMaxAge;
  }

  public int getMinSize() {
    return minSize;
  }

  public void setMinSize(int minSize) {
    this.minSize = minSize;
  }

  public int getMaxSize() {
    return maxSize;
  }

  public void setMaxSize(int maxSize) {
    this.maxSize = maxSize;
  }

  public int getMinAge() {
    return minAge;
  }

  public void setMinAge(int minAge) {
    this.minAge = minAge;
  }

  public int getMaxAge() {
    return maxAge;
  }

  public void setMaxAge(int maxAge) {
    this.maxAge = maxAge;
  }

  public boolean isConsiderMinSize() {
    return considerMinSize;
  }

  public void setConsiderMinSize(boolean considerMinSize) {
    this.considerMinSize = considerMinSize;
  }

  public boolean isConsiderMaxSize() {
    return considerMaxSize;
  }

  public void setConsiderMaxSize(boolean considerMaxSize) {
    this.considerMaxSize = considerMaxSize;
  }

  public boolean isConsiderMinAge() {
    return considerMinAge;
  }

  public void setConsiderMinAge(boolean considerMinAge) {
    this.considerMinAge = considerMinAge;
  }

  public boolean isConsiderMaxAge() {
    return considerMaxAge;
  }


  public void setConsiderMaxAge(boolean considerMaxAge) {
    this.considerMaxAge = considerMaxAge;
  }
}
