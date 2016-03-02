/*
*********************************************************************************************************
*                                               uC/OS-II
*                                         The Real-Time Kernel
*
*                         (c) Copyright 1998-2003, Jean J. Labrosse, Weston, FL
*                                          All Rights Reserved
*
*                                          Sample code
*                                          MC9S12DP256B 
*                                       Wytec Dragon12 EVB
*
* File : app.c
* By   : Eric Shufro
*********************************************************************************************************
*/

#include    <includes.h>


/*
*********************************************************************************************************
*                                                DEFINES
*********************************************************************************************************
*/


/*
*********************************************************************************************************
*                                                CONSTANTS
*********************************************************************************************************
*/
   
 
/*
*********************************************************************************************************
*                                                VARIABLES
*********************************************************************************************************
*/

    OS_STK        AppStartTaskStk[APP_TASK_START_STK_SIZE];
    OS_STK        LCD_TestTaskStk[LCD_TASK_STK_SIZE];
    OS_STK        SevenSegTestTaskStk[SEVEN_SEG_TEST_TASK_STK_SIZE];
    OS_STK        KeypadRdTaskStk[KEYPAD_RD_TASK_STK_SIZE];
    OS_STK        SpeakerPlyTaskStk[SPEAKER_PLY_TASK_STK_SIZE];          // speaker play task stack size -jsw   
    OS_STK        IncHourTaskStk[HOUR_TASK_STK_SIZE];
    OS_STK        IncMinuteTaskStk[MINUTE_TASK_STK_SIZE];
    			 
    OS_FLAG_GRP  *keypadEnFlagGrp; 
    

/*
*********************************************************************************************************
*                                                STRUCTURES
*********************************************************************************************************
*/  
    
TIME hrmin = 
{
    12, 1200
};

/*
*********************************************************************************************************
*                                            SEMAPHORES\MUTEXES
*********************************************************************************************************
*/

OS_EVENT *time_mutex;

/*
*********************************************************************************************************
*                                            FUNCTION PROTOTYPES
*********************************************************************************************************
*/

static  void  AppStartTask(void *p_arg);
static  void  AppTaskCreate(void);
static  void  LCD_TestTask(void *p_arg);
static  void  SevenSegTestTask(void *p_arg);
static  void  KeypadRdTask(void *p_arg);
static  void  SpeakerPlyTask(void *p_arg);                              // task function prototype -jsw
static  void  IncHourTask(void *p_arg);
static  void  IncMinuteTask(void *p_arg);

#if (uC_PROBE_OS_PLUGIN > 0) || (uC_PROBE_COM_MODULE > 0)
extern  void  AppProbeInit(void);
#endif




/*
*********************************************************************************************************
*                                                main()
*
* Description : This is the standard entry point for C code.  It is assumed that your code will call
*               main() once you have performed all necessary 68HC12 and C initialization.
* Arguments   : none
*********************************************************************************************************
*/

void  main (void)
{
    INT8U   err;


    OSInit();                                                           /* Initialize "uC/OS-II, The Real-Time Kernel" */
    Switches_Init();                                                    /* Initialize the switches SW2,SW3,SW4,SW5 */
                                                                        

    
    time_mutex = OSMutexCreate(TIME_MUTEX_PRIO, &err);
    
    
    OSTaskCreateExt(AppStartTask,
                    (void *)0,
                    (OS_STK *)&AppStartTaskStk[APP_TASK_START_STK_SIZE - 1],
                    APP_TASK_START_PRIO,
                    APP_TASK_START_PRIO,
                    (OS_STK *)&AppStartTaskStk[0],
                    APP_TASK_START_STK_SIZE,
                    (void *)0,
                    OS_TASK_OPT_STK_CHK | OS_TASK_OPT_STK_CLR);

    OSTaskNameSet(APP_TASK_START_PRIO, "Start Task", &err);

    OSStart();                                                          /* Start multitasking (i.e. give control to uC/OS-II)       */
}


/*$PAGE*/
/*
*********************************************************************************************************
*                                          STARTUP TASK
*
* Description : This is an example of a startup task.  As mentioned in the book's text, you MUST
*               initialize the ticker only once multitasking has started.
*
* Arguments   : p_arg   is the argument passed to 'AppStartTask()' by 'OSTaskCreate()'.
*
* Notes       : 1) The first line of code is used to prevent a compiler warning because 'p_arg' is not
*                  used.  The compiler should not generate any code for this statement.
*               2) Interrupts are enabled once the task start because the I-bit of the CCR register was
*                  set to 0 by 'OSTaskCreate()'.
*				3) After this created from main(), it runs and initializes additional application
*                  modules and tasks. Rather than deleting the task, it is simply suspended
*                  periodically. This tasks body could be used for additional work if desired.
*********************************************************************************************************
*/

static  void  AppStartTask (void *p_arg)
{		
   (void)p_arg;
   		  
    BSP_Init();                                                         /* Initialize the ticker, and other BSP related functions   */

#if OS_TASK_STAT_EN > 0
    OSStatInit();                                                       /* Start stats task                                         */
#endif

#if (uC_PROBE_OS_PLUGIN > 0) || (uC_PROBE_COM_MODULE > 0)
    AppProbeInit();                                                     /* Initialize uC/Probe modules                              */
#endif
    
    AppTaskCreate();                                                    /* Create additional tasks using this user defined function */
    
    while (TRUE) {                                                      /* Task body, always written as an infinite loop            */
        OSTimeDlyHMSM(0, 0, 5, 0);                                      /* Delay the task                                           */
    }
}


/*$PAGE*/
/*
*********************************************************************************************************
*                                     CREATE APPLICATION TASKS
*
* Description : This function demonstrates how to create a new application task. 
* 
* Notes:        1) Each task should be a unique function prototypes as 
*                  static  void  mytaskname (void *p_arg). 
*               2) Additionally, each task should contain an infinite loop and call at least one
*                  OS resource on each pass of the loop. An OS resource may be a call to OSTimeDly(),
*                  OSTimeDlyHMSM(), or one of the message box, semaphore or other OS handled resource.
*               3) Each task must have its own stack. Be sure that the stack is declared large
*                  enough or the entire system may crash or experience erradic results if your stack
*                  grows and overwrites other variables in memory.
*               
* Arguments   : none
* Notes       : none
*********************************************************************************************************
*/

static  void  AppTaskCreate (void)
{
    INT8U  err;

    
    OSTaskCreateExt(LCD_TestTask,
                    (void *)0,
                    (OS_STK *)&LCD_TestTaskStk[LCD_TASK_STK_SIZE-1],
                    LCD_TEST_TASK_PRIO,
                    LCD_TEST_TASK_PRIO,
                    (OS_STK *)&LCD_TestTaskStk[0],
                    LCD_TASK_STK_SIZE,
                    (void *)0,
                    OS_TASK_OPT_STK_CHK | OS_TASK_OPT_STK_CLR);
    OSTaskNameSet(LCD_TEST_TASK_PRIO, "LCD Test Task", &err);    

    OSTaskCreateExt(SevenSegTestTask,
                    (void *)0,
                    (OS_STK *)&SevenSegTestTaskStk[SEVEN_SEG_TEST_TASK_STK_SIZE-1],
                    SEVEN_SEG_TEST_TASK_PRIO,
                    SEVEN_SEG_TEST_TASK_PRIO,
                    (OS_STK *)&SevenSegTestTaskStk[0],
                    SEVEN_SEG_TEST_TASK_STK_SIZE,
                    (void *)0,
                    OS_TASK_OPT_STK_CHK | OS_TASK_OPT_STK_CLR);
    OSTaskNameSet(SEVEN_SEG_TEST_TASK_PRIO, "SevenSegTest Task", &err);          

    OSTaskCreateExt(KeypadRdTask,
                    (void *)0,
                    (OS_STK *)&KeypadRdTaskStk[KEYPAD_RD_TASK_STK_SIZE-1],
                    KEYPAD_RD_TASK_PRIO,
                    KEYPAD_RD_TASK_PRIO,
                    (OS_STK *)&KeypadRdTaskStk[0],
                    KEYPAD_RD_TASK_STK_SIZE,
                    (void *)0,
                    OS_TASK_OPT_STK_CHK | OS_TASK_OPT_STK_CLR);
    OSTaskNameSet(KEYPAD_RD_TASK_PRIO, "KeypadRd Task", &err);          

    // --CONTINUE HERE-- 
    OSTaskCreate(SpeakerPlyTask, 
                (void*)0, 
                &SpeakerPlyTaskStk[SPEAKER_PLY_TASK_STK_SIZE],
                SPEAKER_PLY_TASK_PRIO);
    OSTaskCreate(IncMinuteTask, 
                (void*)0, 
                &IncMinuteTaskStk[MINUTE_TASK_STK_SIZE],
                INC_MINUTE_TASK_PRIO);
    OSTaskCreate(IncHourTask, 
                (void*)0, 
                &IncHourTaskStk[HOUR_TASK_STK_SIZE],
                INC_HOUR_TASK_PRIO);
                
}


/*$PAGE*/
/*
*********************************************************************************************************
*                                             SevenSegWriteTask
*
* Description: This task displays messages on the Dragon12 (16x2) LCD screen and is
*              responsible for initializing the LCD hardware. Care MUST be taken to
*              ensure that the LCD hardware is initialized before other tasks
*              attempt to write to it. If necessary the DispInit() function
*              may be called from the start task or bsp_init().
*********************************************************************************************************
*/

static  void  LCD_TestTask (void *p_arg)
{
       CPU_INT08S  i;
       CPU_INT08U  err;
                                                                        /* Keypad Enabled Message                                   */
const  CPU_INT08U  KeypadEnStr[18]    = {"Keypad Enabled"};                     

                                                                        /* Keypad Disabled Message                                  */                       
const  CPU_INT08U  KeypadDisStr[18]   = {"OH YEAH!!"};                    

                                                                        /* Power On Welcome Message. three seperate msgs / rows     */
const  CPU_INT08U  WelcomeStr[6][18]  = {"Welcome to the", "Dragon 12 EVB.  ",                                                                          
                                         "This demo runs", "Micrium uC/OS-II",  
                                         "on a 48 MHz   ", "MC9S12DG256B CPU"}; 
    
                                                                        /* Define a message to scroll on the LCDs top line          */
const  CPU_INT08U  aboutStr[]         = 
{ "I'm looking at the man in the mirror. I'm asking him to change his ways!!!"
    
};/*{"Did you know that uC/OS-II can "
                                         "provide multi-tasking and "
                                         "real-time services to your "
                                         "embedded applications? In fact, "
                                         "uC/OS-II provides services "
                                         "such as task delays, "
                                         "semaphores, message mailboxes, "  
                                         "timers, event flags, memory "		
                                         "management, mutexes, queues, "
                                         "and much more!"
                                         };*/
                                         
   CPU_INT08U  *aboutStrPtr;                                            /* Declare a pointer to aboutStr, used for scroll effect    */
    
                                   
   INT8U* msg;                               

   (void)p_arg;
                   
    DispInit(2, 16);                                                    /* Initialize uC/LCD for a 2 row by 16 column display       */
    
    
    
    /*TEST
    
    */
    
//    msg = OSQPend(QFoster, 0, &err);
    msg[0] = 42;

             
    while (DEF_TRUE) {                                                  /* All tasks bodies include an infinite loop                */           
        DispClrScr();                                                   /* Start by clearing the screen                             */

        for (i = 0; i < 6; i+=2) {										/* With the Keypad task suspended, both LCD rows are avail. */
            DispStr(0, 0, WelcomeStr[i]);                               /* Display row 0 of Welcome Message i                       */
            DispStr(1, 0, WelcomeStr[i+1]);                             /* Display row 1 of Welcome Message i                       */        
            OSTimeDlyHMSM(0, 0, 2, 0);                                  /* Delay between updating the message                       */
        }        

        DispClrLine(1);                                                 /* Clear LCD ROW 1 before unblocking the keypad task        */
                
        OSFlagPost(keypadEnFlagGrp, 0x01, OS_FLAG_SET, &err);           /* Set flag bit 0 of the keypad enable flag group           */
                                                                        /* This will unblock the keypad task which will use the     */
                                                                        /* bottom row of the LCD while not disabled                 */
                                                                            
        while (err != OS_NO_ERR) {                                      /* If a flag posting error occured,                         */
            OSTimeDlyHMSM(0, 0, 1, 0);                                  /* delay and try again until NO ERROR is returned           */
            OSFlagPost(keypadEnFlagGrp, 0x01, OS_FLAG_SET, &err);       /* Set flag bit 0 of the keypad enable flag group           */
        }


        DispClrLine(0);                                                 /* Clear line 0 of the LCD                                  */
        for (i = 0; i < 3; i++) {                                       /* Blink the Keypad Enabled String 3 times                  */
            DispStr(0, 0, KeypadEnStr);                                 /* Display the Keypad Enabled message                       */     
            OSTimeDlyHMSM(0, 0, 0,500);                                 /* Show the message for a while                             */
            DispClrLine(0);                                             /* Next clear line 0 of the LCD                             */
            OSTimeDlyHMSM(0, 0, 0,500);                                 /* Lastly, wait a bit before repeating the message          */
        }

        aboutStrPtr = aboutStr;                                         /* Point to the start of the about message string           */

																		/* Start scrolling from right to left. This involves        */
        																/* starting from the last column and working toward the     */
                                                                        /* first column until the screen fills up. The remaining    */
                                                                        /* characters may start from position 0.                    */
        for (i = 15; i >= 0; i--) {										/* For the first 16 characters...                           */
            if (*aboutStrPtr != '\0') {						            /* If we have not reached the end of the string             */
                DispStr(0, i, aboutStrPtr);                             /* Display (col i - distance to end of lcd line) # of chars */
                OSTimeDlyHMSM(0, 0, 0, 100);                            /* Delay before updating the screen with the shifted msg    */             
            }
        }

        while ((aboutStr + sizeof(aboutStr) - aboutStrPtr) >  16) {     /* While there are greater than 16 chars left to display    */
            DispStr(0, 0, aboutStrPtr++);                               /* Display 16 chars and increment (shift) message by 1 char */
            OSTimeDlyHMSM(0, 0, 0, 100);                                /* Delay before displaying the shifted message              */
        }

        for (i = 15; i >= 0; i--) {										/* For the last 16 characters                               */
            if (*aboutStrPtr != '\0') {						            /* If we have not reached the end of the string             */
                DispStr(0, 0, aboutStrPtr++);                           /* Display (col i - distance to end of lcd line) # of chars */
                DispChar(0, i, ' ');                                    /* Leave spaces chars behind as the str scrolls off screen  */
                OSTimeDlyHMSM(0, 0, 0, 100);                            /* Delay before updating the screen with the shifted msg    */             
            }
        }
                
        DispClrLine(1);                                                 /* Clear LCD ROW 1 before blocking the keypad task          */
        
        OSFlagPost(keypadEnFlagGrp, 0x01, OS_FLAG_CLR, &err);           /* Disable the keypad task by clearing its event flag 0     */

        while (err != OS_NO_ERR) {                                      /* If an error occured (perhaps the flag group is not       */
            OSTimeDlyHMSM(0, 0, 1, 0);                                  /* initialized yet, then delay and try again until no error */
            OSFlagPost(keypadEnFlagGrp, 0x01, OS_FLAG_CLR, &err);       /* Clear flag bit 0 of the keypad enable flag group         */
        }        
        
        DispClrScr();                                                   /* Clear the screen                                         */
        for (i = 0; i < 3; i++) {                                       /* Blink the Keypad Disabled String 3 times                 */
            DispStr(0, 0, KeypadDisStr);                                /* Display the Keypad Disabled message                      */     
            OSTimeDlyHMSM(0, 0, 0,500);                                 /* Show the message for a while                             */
            DispClrLine(0);                                             /* Next clear line 0 of the LCD                             */
            OSTimeDlyHMSM(0, 0, 0,500);                                 /* Lastly, wait a bit before repeating the message          */
        }          
    }
}


/*$PAGE*/
/*
*********************************************************************************************************
*                                             SevenSegWriteTask
*
* Description: This task displays messages on the Dragon12 (16x2) LCD screen
*********************************************************************************************************
*/

static  void  SevenSegTestTask (void *p_arg)
{
    // CPU_INT16U  num;
    // CPU_INT16U  num2;


   INT8U err;
   (void)p_arg;   
   
    SevenSegDisp_Init();	                                            /* Initialize the 7-Seg I/O and periodic refresh interrupt  */
    
   // num = hrmin.minute;//1200;
   // num2 = hrmin.hour;//12;
      
    while (DEF_TRUE) {                                                  /* All tasks bodies include an infinite loop */
        
        OSMutexPend(time_mutex, 0, &err);   
        SevenSegWrite(hrmin.minute);                                             /* Output the value to the screen */                                                                         
        hrmin.minute = ((hrmin.minute + 1) % 10000);                                      /* Increment the # being displayed, wrap after 9,999        */
        
        //if (num == 59)
        if (hrmin.minute > 0)
        {
            if (((hrmin.minute - hrmin.hour * 100) % 60) == 0)
            {
                hrmin.hour++;
                
                if (hrmin.hour >= 13)
                    hrmin.hour = 1;
                
                
                hrmin.minute = (hrmin.hour) * 100;              
            }
        }
        
        //SevenSegWrite(hrmin.minute);
        OSTimeDlyHMSM(0, 0, 1, 0);//10);
        OSMutexPost(time_mutex); 
        
                                           /* Delay the task for 50ms and repeat the process           */
        
        
        //SevenSegWrite(hrmin.minute);
        
    }
}

/*$PAGE*/
/*
*********************************************************************************************************
*                                             Increment Hour Task
*
* Description: This task increments the hour variable in the time structure. The
*              time structure holds the current value of the clock displayed on the
*              7-segment display.
*********************************************************************************************************
*/
static  void  IncHourTask(void *p_arg) 
{
    INT8U err;
    CPU_INT16U rem_min = 0;
    
    for(;;) 
    {
    
        SW2Lock();
        OSMutexPend(time_mutex, 0, &err);
        
        hrmin.hour++;
        
        if (hrmin.hour >= 13)
          hrmin.hour = 1;
        
        rem_min = hrmin.minute % 100;
                
        hrmin.minute = (hrmin.hour) * 100 + rem_min; 
        
        OSMutexPost(time_mutex);        
    }    
}

/*$PAGE*/
/*
*********************************************************************************************************
*                                             Increment Minute Task
*
* Description: This task increments the minute variable in the time structure. The 
*              time structure holds the current value of the clock displayed on the
*              7-segment display.
*********************************************************************************************************
*/
static  void  IncMinuteTask(void *p_arg) 
{    
    INT8U err;
    CPU_INT16U rem_minute;
    CPU_INT16U offset_min;
    
    for(;;) 
    {
    
        SW3Lock();
        OSMutexPend(time_mutex, 0, &err);
       
        hrmin.minute++;
        
        rem_minute = hrmin.minute % 100;
        
        if (rem_minute > 59) {
           offset_min = rem_minute - 60;
           hrmin.hour++;
           
           if (hrmin.hour >= 13)
            hrmin.hour = 1;
           
           hrmin.minute = hrmin.hour * 100 + offset_min;
        } 
          
        
        OSMutexPost(time_mutex);        
    }    
}


/*$PAGE*/
/*
*********************************************************************************************************
*                                             Keypad Read Task
*
* Description: This task periodically reads the Wytec Dragon12 EVB keyapd and
*              displays the value on the bottom row of the LCD screen.
*********************************************************************************************************
*/

static  void  KeypadRdTask (void *p_arg)
{
    CPU_INT08U  key;
    CPU_INT08U  out_str[17];
    CPU_INT08U  key_map[] = {'1', '2', '3', 'A',
                             '4', '5', '6', 'B', 
                             '7', '8', '9', 'C',
                             '*', '0', '#', 'D'
                             };
    CPU_INT08U  err;
        

   (void)p_arg;
       
    KeypadInitPort();                                                   /* Initialize the keypad hardware                           */
    
    keypadEnFlagGrp = OSFlagCreate(0, &err);                            /* Create an event flag group. All flags initialized to 0   */
        
    while (err != OS_NO_ERR) {                                          /* If an error code was returned, loop until successful     */
        OSTimeDlyHMSM(0, 0, 1, 0);				                        /* Delay for 1 second, wait for resources to be freed       */
        keypadEnFlagGrp = OSFlagCreate(0, &err);                        /* Try to create the flag group again                       */
    }
    
    OSFlagPend(keypadEnFlagGrp, 0x01, OS_FLAG_WAIT_SET_ALL, 0, &err);   /* Wait until bit 1 of the flag group to become set         */
                                                                        /* The goal is to prevent this task from accessing the      */
                                                                        /* bottom row of the LCD until the LCD task has finished    */
                                                                        /* displaying its introduction message. This message will   */
                                                                        /* require both lines of the LCD, so we must wait to use it */
            
    DispClrLine(1);                                                     /* Clear the bottom line of the 2 line display              */
                
    while (DEF_TRUE) {                                                  /* All tasks bodies include an infinite loop                */   
        OSFlagPend(keypadEnFlagGrp, 0x01, OS_FLAG_WAIT_SET_ALL,0,&err); /* Suspend the task if flag 0 has been cleared              */
        key = KeypadReadPort();                                         /* Scan the keypad. Returns 0-15 or 0xFF if nothing pushed  */
        if (key == 0xFF) {            
            err = sprintf(out_str, "");//"Keypad is IDLE");                   /* Write "You Pressed" and the key number 'n' to a string   */      
        } else {
            err = sprintf(out_str, "You Pressed: %c", key_map[key]);    /* Write "You Pressed" and the key number 'n' to a string   */      
        }
        
        DispStr(1, 0, out_str);                                         /* Display the keypad message on row 1 of the LCD screen    */        
        OSTimeDlyHMSM(0, 0, 0,100);                                     /* Read the keypad every 100ms                              */
    }
}

/*$PAGE*/
/*
*********************************************************************************************************
*                                             Speaker Play Task
*
* Description: This task plays a tune on the Wytec Dragon12 EVB speaker.
*********************************************************************************************************
*/
static  void  SpeakerPlyTask(void *p_arg) 
{
    Speaker_Init();                                                     /* Initialize the speaker */
    
    while(DEF_TRUE)                                                     /* Play B-flat scale  */
    {
        SW4Lock();
        //OSTimeDlyHMSM(0, 0, 0, 600);
        Speaker_Note(NOTE_AS3);
        OSTimeDlyHMSM(0, 0, 0, 600);
        Speaker_Note(NOTE_C4);
        OSTimeDlyHMSM(0, 0, 0, 600);
        /*Speaker_Note(NOTE_D4);
        OSTimeDlyHMSM(0, 0, 0, 600);
        Speaker_Note(NOTE_DS4);
        OSTimeDlyHMSM(0, 0, 0, 600);
        Speaker_Note(NOTE_F4);
        OSTimeDlyHMSM(0, 0, 0, 600);
        Speaker_Note(NOTE_G4);
        OSTimeDlyHMSM(0, 0, 0, 600);
        Speaker_Note(NOTE_A4);
        OSTimeDlyHMSM(0, 0, 0, 600);
        Speaker_Note(NOTE_AS4);
        OSTimeDlyHMSM(0, 0, 0, 600);
        Speaker_Note(NOTE_AS4);
        OSTimeDlyHMSM(0, 0, 0, 600);
        Speaker_Note(NOTE_A4);
        OSTimeDlyHMSM(0, 0, 0, 600);
        Speaker_Note(NOTE_G4);
        OSTimeDlyHMSM(0, 0, 0, 600);
        Speaker_Note(NOTE_F4);
        OSTimeDlyHMSM(0, 0, 0, 600);
        Speaker_Note(NOTE_DS4);
        OSTimeDlyHMSM(0, 0, 0, 600);
        Speaker_Note(NOTE_D4);
        OSTimeDlyHMSM(0, 0, 0, 600);
        Speaker_Note(NOTE_C4);
        OSTimeDlyHMSM(0, 0, 0, 600);
        Speaker_Note(NOTE_AS3);
        OSTimeDlyHMSM(0, 0, 0, 600);*/
        Speaker_Note(NOTE_OFF);  
    }
    
}



