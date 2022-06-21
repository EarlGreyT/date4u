package de.earlgreyt.date4u.repositories.search;

public enum SearchOperation {
  GREATER_THAN,
  LESS_THAN,
  GREATER_THAN_EQUAL,
  LESS_THAN_EQUAL,
  NOT_EQUAL,
  EQUAL,
  MATCH,
  MATCH_START,
  MATCH_END,
  IN,
  NOT_IN,
  BEFORE_NOW,
  NOT_BEFORE_NOW
}