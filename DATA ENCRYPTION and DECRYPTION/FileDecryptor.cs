using System;
using System.IO;
using System.Security.Cryptography;

namespace FileEncryptionApp
{
    public class FileDecryptor : Encryption
    {
        public override void Encrypt(string filePath, string password)
        {
            Console.WriteLine("This function is not supported in FileDecryptor.");
        }

        public override void Decrypt(string filePath, string password)
        {
            try
            {
                byte[] key = GenerateKeyFromPassword(password);

                using (FileStream fs = new FileStream(filePath, FileMode.Open))
                {
                    byte[] iv = new byte[16];
                    int bytesRead = fs.Read(iv, 0, iv.Length);

                    if (bytesRead < iv.Length)
                    {
                        throw new InvalidDataException("File is corrupted or not encrypted properly.");
                    }

                    using (Aes aes = Aes.Create())
                    {
                        aes.Key = key;
                        aes.IV = iv;

                        using (CryptoStream cs = new CryptoStream(fs, aes.CreateDecryptor(), CryptoStreamMode.Read))
                        using (FileStream fsOutput = new FileStream(filePath.Replace(".enc", ""), FileMode.Create))
                        {
                            cs.CopyTo(fsOutput);
                        }
                    }
                }
                Console.WriteLine("File decrypted successfully.");
            }
            catch (CryptographicException)
            {
                Console.WriteLine("Decryption failed. The password may be incorrect or the file is corrupted.");
            }
            catch (IOException ex)
            {
                Console.WriteLine($"An I/O error occurred: {ex.Message}");
            }
            catch (Exception ex)
            {
                Console.WriteLine($"An unexpected error occurred: {ex.Message}");
            }
        }
    }
}
