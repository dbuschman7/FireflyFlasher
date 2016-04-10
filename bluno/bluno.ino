 int led = 9;
const byte numChars = 250;
char receivedChars[numChars];  // an array to store the received data

boolean newData = false;

void setup() {
  Serial.begin(115200);
  pinMode(led, OUTPUT);
  Serial.println("<Arduino is ready>");
  
}

void loop() {
  getCommandList();
  processNewData();
}

void getCommandList() {
  static byte ndx = 0;
  char endMarker = '\n';
  char rc;
  
  while (Serial.available() > 0 && newData == false) {
    rc = Serial.read();

    if (rc != endMarker) {
      receivedChars[ndx] = rc;
      ndx++;
      if (ndx >= numChars) {
        ndx = numChars - 1;
      }
    }
    else {
      receivedChars[ndx] = '\0'; // terminate the string
      ndx = 0;
      newData = true;
    }
  }
}

void processNewData() {
  if (newData == true) {
    char** commandStr = parseLine(receivedChars, "|");
    int i = 0;
    while (commandStr[i] != 0)
    { 
      processCommand(commandStr[i]); 
      Serial.write(commandStr[i]);
      Serial.println();   //print line feed character
      i++;
    }
    newData = false;
  }
}



void processCommand(char* commandStr) { 
  char* paramStr = strtok(commandStr, "/");
  if (paramStr != 0)
  { 
    char* durationStr = strtok(0, "/");
    int duration = atoi(durationStr);
    if(strcmp(paramStr,"flash") == 0)
    {
      digitalWrite(led, HIGH);
      delay(duration);
      digitalWrite(led, LOW);
    }
    else if(strcmp(paramStr,"pause") == 0)
    {
      delay(duration);
    }
    else
    {
      //do nothing
    }
  }
}


char** parseLine(char* input, char* delim) { 
  char *array[100];
  
  int i = 0;
  char* p = strtok (input, delim);
  while (p != NULL)
  {
      array[i++] = p;
      p = strtok (NULL, delim);
  }
  array[i][0] = 0;
  array[i][1] = 0;
  array[i][2] = 0;
  array[i][3] = 0;
  
  return array;
}

