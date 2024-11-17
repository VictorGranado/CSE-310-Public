using System;

namespace FileEncryptionApp
{
    class Program
    {
        static void Main(string[] args)
        {
            while (true)
            {
                Console.WriteLine("\nWelcome to the File Encryption and Decryption Tool.");
                Console.WriteLine("Choose an option:");
                Console.WriteLine("1. Encrypt a file");
                Console.WriteLine("2. Decrypt a file");
                Console.WriteLine("3. Exit");
                Console.Write("Enter your choice: ");
                string choice = Console.ReadLine();

                if (choice == "1")
                {
                    Console.Write("Enter the file path to encrypt: ");
                    string filePath = Console.ReadLine();
                    Console.Write("Enter a password for encryption: ");
                    string password = Console.ReadLine();

                    FileEncryptor encryptor = new FileEncryptor();
                    encryptor.Encrypt(filePath, password);
                }
                else if (choice == "2")
                {
                    Console.Write("Enter the file path to decrypt: ");
                    string filePath = Console.ReadLine();
                    Console.Write("Enter the password for decryption: ");
                    string password = Console.ReadLine();

                    FileDecryptor decryptor = new FileDecryptor();
                    decryptor.Decrypt(filePath, password);
                }
                else if (choice == "3")
                {
                    Console.WriteLine("Exiting program. Goodbye!");
                    break;
                }
                else
                {
                    Console.WriteLine("Invalid choice. Please try again.");
                }
            }
        }
    }
}
