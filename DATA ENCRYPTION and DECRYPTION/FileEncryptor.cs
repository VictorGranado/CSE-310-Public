using System;
using System.IO;
using System.Security.Cryptography;

namespace FileEncryptionApp
{
    public class FileEncryptor : Encryption
    {
        public override void Encrypt(string filePath, string password)
        {
            byte[] key = GenerateKeyFromPassword(password);
            byte[] iv = new byte[16]; // Initialization vector for AES
            using (var rng = RandomNumberGenerator.Create())
            {
                rng.GetBytes(iv);
            }

            using (Aes aes = Aes.Create())
            {
                aes.Key = key;
                aes.IV = iv;

                using (FileStream fs = new FileStream(filePath + ".enc", FileMode.Create))
                {
                    fs.Write(iv, 0, iv.Length);
                    using (CryptoStream cs = new CryptoStream(fs, aes.CreateEncryptor(), CryptoStreamMode.Write))
                    using (FileStream fsInput = new FileStream(filePath, FileMode.Open))
                    {
                        fsInput.CopyTo(cs);
                    }
                }
            }
            Console.WriteLine("File encrypted successfully.");
        }

        public override void Decrypt(string filePath, string password)
        {
            Console.WriteLine("This function is not supported in FileEncryptor.");
        }
    }
}
