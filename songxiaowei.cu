#include "cuda_runtime.h"
#include "device_launch_parameters.h"
#include <stdio.h>

__global__ void printHello()
{
    printf("宋晓维提交\n");
    printf("Hello\n");
}

int main()
{
    printHello << <1, 8 >> > ();
    cudaDeviceSynchronize(); 
    return 0;
}
