%{
	#include<string.h>
	#include<stdio.h>
	char ans[100],q[100],st[100],fname[30],c;
	char quesarr[15][25],temp[25]; 
	int top=0,space=0,fs=0,i=0,mark=0;
	FILE *fp,*fp1;
%}
%token QWORD SP VERB QMARK ID
%%
program:QWORD SP VERB other	{printf("question is valid\n");
				fp1=fopen("ques","r");
				while((c=fgetc(fp1))!=EOF)
					q[i++]=c;
				q[i]='\0';
				fclose(fp1);
				make_ans();
				}
other:SP ID other		
     |SP ID QMARK        
     ;

%%
#include "lex.yy.c"
#include<stdlib.h>
make_ans()
{
	int i,j=0,k=0;
	FILE *p;
	for(i=0;i<strlen(q);i++)
	{
		if(q[i]==' ')
		{
			++space;
			fs=i;
			if(space==2)
				break;
			
		}
		//printf("%c\n",q[fs+1]);
	}
	//printf("%c\n",q[fs+1]);
	for(i=fs+1;i<strlen(q);i++)
	{
		if(q[i]==' '||q[i]=='?')
		{
			temp[j]='\0';
			//printf("%s\n",temp);
			strcpy(quesarr[k++],temp);
			j=0;
			i++;
		}
		temp[j++]=q[i];
		
	}
	p=fopen("passage","r");
	printf("ans \n");
	j=0;
	while((c=fgetc(p))!=EOF)
	{
		
		if(c==' ')
		{
			temp[j]='\0';
			if(strcmp(temp,quesarr[mark])==0)
			{	
				
				printf("%s ",temp);
				mark++;
				if(mark==k)
				{
					while((c=fgetc(p))!='.')
						printf("%c",c);
					break;
				}
			}
			j=0;
		}
		else
		temp[j++]=c;
	}
	fclose(p);
}	
int yywrap()
{
	return -1;
}
int yyerror()
{
	printf("question is invalid\n");
	return 0;
}
main()
{
	//printf("enter passage file:");
	//scanf("%s",fname);
	//fp=fopen(fname,"r");
	printf("passage\n");
	system("cat passage");
	printf("\nquestion \n");
	system("cat ques");
	printf("\n");
	yyin=fopen("ques","r");
	yyparse();
}
