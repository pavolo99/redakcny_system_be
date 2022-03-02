package sk.tuke.fei.kpi.dp.common;


public enum Domain {
  GIT_KPI_FEI_TUKE_SK("https://git.kpi.fei.tuke.sk"),
  GITLAB_COM("https://gitlab.com"),
  GITHUB_COM("https://github.com");

  private final String domainURLAddress;

  Domain(String domainURLAddress) {
    this.domainURLAddress = domainURLAddress;
  }

  public String getDomainURLAddress() {
    return domainURLAddress;
  }
}
