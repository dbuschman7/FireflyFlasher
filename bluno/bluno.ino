//
// Variables at the top, how quaint 
// //////////////////////////////
 int ledPin = 5;
 int redPin = 9;
 int greenPin = 10; 
 int bluePin = 11; 
 
const byte numChars = 250;
char receivedChars[numChars];  // an array to store the received data

boolean newData = false;

//
// Setup 
// //////////////////////////////
void setup() {
  Serial.begin(115200);
  pinMode(ledPin, OUTPUT);
  
  pinMode(redPin, OUTPUT);
  pinMode(greenPin, OUTPUT);
  pinMode(bluePin, OUTPUT);  

  Serial.println("<Arduino is ready>");
}

void loop() {
  getCommandList();
  processNewData();
}

//
// Main Logic
// //////////////////////////////
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
      digitalWrite(ledPin, HIGH);
      delay(duration);
      digitalWrite(ledPin, LOW);
    }
    else if(strcmp(paramStr,"pause") == 0)
    {
      delay(duration);
    }
    else if(strcmp(paramStr,"red") == 0)
    {
       setColor(nextInteger(), 0, 0, duration); 
    }
    else if(strcmp(paramStr,"green") == 0)
    {
       setColor(0, nextInteger(), 0, duration); 
    }
    else if(strcmp(paramStr,"blue") == 0)
    {
       setColor(0, 0, nextInteger(), duration); 
    }
    else if(strcmp(paramStr,"color") == 0)
    {
       setColor(nextInteger(), nextInteger(), nextInteger(), duration); 
    }    
    else
    {
      //do nothing
    }
  }
}

//
// Helpers
// //////////////////////////////
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

int nextInteger() { 
   char* intensityStr = strtok(0, "/");
   int intensity = atoi(intensityStr);
   return intensity;
}


void setColor(int red, int green, int blue, int duration)
{
  #ifdef COMMON_ANODE
    red = 255 - red;
    green = 255 - green;
    blue = 255 - blue;
  #endif
  analogWrite(redPin, red);
  analogWrite(greenPin, green);
  analogWrite(bluePin, blue);  
  delay(duration);
  analogWrite(redPin, 0);
  analogWrite(greenPin, 0);
  analogWrite(bluePin, 0);  
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

// DONE 
