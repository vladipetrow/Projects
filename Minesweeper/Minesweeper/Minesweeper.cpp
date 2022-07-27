#include<iostream>
#include<time.h>
#include <stdlib.h>
#include<string.h>
using namespace std;
bool isValid(int row, int col) //Function to check if the cell is valid or not.
{
    return (row >= 0) && (row < 9) &&
        (col >= 0) && (col < 9);
}
bool isMine(int row, int col, char board[][9])  //Function to check whether given cell (row, col) has mine or not.

{
    if (board[row][col] == '*')
        return true;
    else
        return false;
}
void printBoard(char myBoard[][9]) //Function which prints the current gameplay board.
{
    int i, j;
    cout << "   ";
    for (i = 0; i < 9; i++)
        cout << i << " ";
    cout << endl;
    for (i = 0; i < 9; i++)
    {
        cout << i << "  ";
        for (j = 0; j < 9; j++)
            cout << myBoard[i][j] << " ";
        cout << endl;
    }
    return;
}
int countNearbyMines(int row, int col, int mines[][2], char realBoard[][9])   //Function to count all the mines in 8 nearby cells.
                                                                            // Only process the cells if they are a valid one.
{

    int cnt = 0;
    if (isValid(row - 1, col) == true)
    {
        if (isMine(row - 1, col, realBoard) == true)
            cnt++;
    }
    if (isValid(row + 1, col) == true)
    {
        if (isMine(row + 1, col, realBoard) == true)
            cnt++;
    }
    if (isValid(row, col + 1) == true)
    {
        if (isMine(row, col + 1, realBoard) == true)
            cnt++;
    }
    if (isValid(row, col - 1) == true)
    {
        if (isMine(row, col - 1, realBoard) == true)
            cnt++;
    }
    if (isValid(row - 1, col + 1) == true)
    {
        if (isMine(row - 1, col + 1, realBoard) == true)
            cnt++;
    }
    if (isValid(row - 1, col - 1) == true)
    {
        if (isMine(row - 1, col - 1, realBoard) == true)
            cnt++;
    }
    if (isValid(row + 1, col + 1) == true)
    {
        if (isMine(row + 1, col + 1, realBoard) == true)
            cnt++;
    }
    if (isValid(row + 1, col - 1) == true)
    {
        if (isMine(row + 1, col - 1, realBoard) == true)
            cnt++;
    }
    return cnt;
}

void placeMines(int mines[][2], char realBoard[][9]) //Function which places mines randomly.
{
    bool mark[81];
    memset(mark, false, sizeof(mark));
    // Place all random mines and continue until they are all placed.
    for (int i = 0; i < 10; )
    {
        int random = rand() % (81);
        int x = random / 9;
        int y = random % 9;


        if (mark[random] == false)     //Add the mine if there is no mine placed at this position.
        {
            mines[i][0] = x;
            mines[i][1] = y;
            realBoard[mines[i][0]][mines[i][1]] = '*';
            mark[random] = true;
            i++;
        }
    }
    return;
}
void Marking(char realBoard[][9], char myBoard[][9])  // Mark the random number generator so that
                                                      // the same configuration doesn't arises.
{
    srand(time(NULL));
    for (int i = 0; i < 9; i++)
    {
        for (int j = 0; j < 9; j++)
        {
            myBoard[i][j] = realBoard[i][j] = '_';
        }
    }
    return;
}
void Play(char myBoard[][9], char realBoard[][9], int mines[][2])
{
    int x, y, timer = 0;
    char c;
    do
    {
        cout << ">> ";         //Input and check for invalid symbols.
        cin >> x >> y >> c;
        if (c == 'r')
        {
            timer++;
            if (isMine(x, y, realBoard) == true) {
                cout << "<<Game over, you hit an explosive" << endl << "<<";  //If there is explosive on the screen appears X.
                myBoard[x][y] = 'X';
                printBoard(myBoard);
                return;
            }
            else {
                int cnt = countNearbyMines(x, y, mines, realBoard);
                myBoard[x][y] = cnt + '0';
                printBoard(myBoard);
            }
        }
        if (c == 'b')
        {
            myBoard[x][y] = 'B';
            printBoard(myBoard);
        }
        if (c != 'r' && c != 'b') {
            cout << "Wrong input!Try again!" << endl;
            return;
        }
        if (x < 0 || x>9 || y < 0 || y>9) {
            cout << "Wrong input!Try again!" << endl;
            return;
        }
    } while (timer < 70);
}
void playMinesweeper()
{
    char realBoard[9][9], myBoard[9][9];
    int mines[10][2];
    Marking(realBoard, myBoard);
    placeMines(mines, realBoard);
    cout << "<<Game starting" << endl;
    printBoard(myBoard);
    Play(myBoard, realBoard, mines);
    return;
}
int main()
{
    playMinesweeper();
    return 0;
}