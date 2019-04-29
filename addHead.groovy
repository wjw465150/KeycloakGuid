def parten = ~/^#{1,}\s{1}.{1,}[^}]$/

File file = new File($/c:\WJW_E\hello.md/$)
file.write("") 

def name=$/c:\WJW_E\WJW_DATA\OpenSource\Keycloak\KeycloakGuid\Securing Applications and Services Guide.md/$
String newLine;
new File(name).eachLine{line->
  if (parten.matcher(line.trim()).matches() ) {
    println "${line} {#}"
    file.append("${line} {#}\r\n")
  } else {
    file.append("${line}\r\n")
  }
}
