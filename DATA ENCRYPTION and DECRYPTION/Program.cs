using System;

namespace FileEncryptionApp
{
    class Program
    {
        static void Main(string[] args)
        {
            Console.WriteLine("Welcome to the File Encryption and Decryption Tool.");
            Console.WriteLine("Choose an option: \n1. Encrypt a file\n2. Decrypt a file");
            string choice = Console.ReadLine();

            Console.WriteLine("Enter the file path:");
            string filePath = Console.ReadLine();
            Console.WriteLine("Enter a password for encryption/decryption:");
            string password = Console.ReadLine();

            if (choice == "1")
            {
                FileEncryptor encryptor = new FileEncryptor();
                encryptor.Encrypt(filePath, password);
            }
            else if (choice == "2")
            {
                FileDecryptor decryptor = new FileDecryptor();
                decryptor.Decrypt(filePath, password);
            }
            else
            {
                Console.WriteLine("Invalid choice. Exiting program.");
            }
        }
    }
}
